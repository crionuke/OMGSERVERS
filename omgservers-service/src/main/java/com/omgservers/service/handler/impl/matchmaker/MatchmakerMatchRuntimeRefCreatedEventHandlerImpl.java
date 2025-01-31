package com.omgservers.service.handler.impl.matchmaker;

import com.omgservers.schema.model.matchmakerCommand.body.OpenMatchMatchmakerCommandBodyDto;
import com.omgservers.schema.model.matchmakerMatchRuntimeRef.MatchmakerMatchRuntimeRefModel;
import com.omgservers.schema.module.matchmaker.GetMatchmakerMatchRuntimeRefRequest;
import com.omgservers.schema.module.matchmaker.GetMatchmakerMatchRuntimeRefResponse;
import com.omgservers.schema.module.matchmaker.SyncMatchmakerCommandRequest;
import com.omgservers.schema.module.matchmaker.SyncMatchmakerCommandResponse;
import com.omgservers.service.event.EventModel;
import com.omgservers.service.event.EventQualifierEnum;
import com.omgservers.service.event.body.module.matchmaker.MatchmakerMatchRuntimeRefCreatedEventBodyModel;
import com.omgservers.service.factory.matchmaker.MatchmakerCommandModelFactory;
import com.omgservers.service.handler.EventHandler;
import com.omgservers.service.shard.matchmaker.MatchmakerShard;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class MatchmakerMatchRuntimeRefCreatedEventHandlerImpl implements EventHandler {

    final MatchmakerShard matchmakerShard;

    final MatchmakerCommandModelFactory matchmakerCommandModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.MATCHMAKER_MATCH_RUNTIME_REF_CREATED;
    }

    @Override
    public Uni<Void> handle(final EventModel event) {
        log.trace("Handle event, {}", event);

        final var body = (MatchmakerMatchRuntimeRefCreatedEventBodyModel) event.getBody();
        final var matchmakerId = body.getMatchmakerId();
        final var matchId = body.getMatchId();
        final var id = body.getId();

        return getMatchmakerMatchRuntimeRef(matchmakerId, matchId, id)
                .flatMap(matchmakerMatchRuntimeRef -> {
                    log.debug("Created, {}", matchmakerMatchRuntimeRef);

                    final var idempotencyKey = event.getId().toString();
                    return syncPrepareMatchMatchmakerCommand(matchmakerId, matchId, idempotencyKey)
                            .replaceWithVoid();
                });
    }

    Uni<MatchmakerMatchRuntimeRefModel> getMatchmakerMatchRuntimeRef(final Long matchmakerId,
                                                                     final Long matchId,
                                                                     final Long id) {
        final var request = new GetMatchmakerMatchRuntimeRefRequest(matchmakerId, matchId, id);
        return matchmakerShard.getService().execute(request)
                .map(GetMatchmakerMatchRuntimeRefResponse::getMatchmakerMatchRuntimeRef);
    }

    Uni<Boolean> syncPrepareMatchMatchmakerCommand(final Long matchmakerId,
                                                   final Long matchId,
                                                   final String idempotencyKey) {
        final var commandBody = new OpenMatchMatchmakerCommandBodyDto(matchId);
        final var commandModel = matchmakerCommandModelFactory.create(matchmakerId, commandBody, idempotencyKey);
        final var request = new SyncMatchmakerCommandRequest(commandModel);
        return matchmakerShard.getService().executeWithIdempotency(request)
                .map(SyncMatchmakerCommandResponse::getCreated);
    }
}
