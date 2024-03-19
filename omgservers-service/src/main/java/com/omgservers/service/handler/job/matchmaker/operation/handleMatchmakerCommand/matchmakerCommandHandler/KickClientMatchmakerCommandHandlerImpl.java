package com.omgservers.service.handler.job.matchmaker.operation.handleMatchmakerCommand.matchmakerCommandHandler;

import com.omgservers.model.matchmakerChangeOfState.MatchmakerChangeOfStateModel;
import com.omgservers.model.matchmakerCommand.MatchmakerCommandModel;
import com.omgservers.model.matchmakerCommand.MatchmakerCommandQualifierEnum;
import com.omgservers.model.matchmakerCommand.body.KickClientMatchmakerCommandBodyModel;
import com.omgservers.model.matchmakerState.MatchmakerStateModel;
import com.omgservers.service.handler.job.matchmaker.operation.handleMatchmakerCommand.MatchmakerCommandHandler;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class KickClientMatchmakerCommandHandlerImpl implements MatchmakerCommandHandler {

    @Override
    public MatchmakerCommandQualifierEnum getQualifier() {
        return MatchmakerCommandQualifierEnum.KICK_CLIENT;
    }

    @Override
    public void handle(final MatchmakerStateModel currentState,
                       final MatchmakerChangeOfStateModel changeOfState,
                       final MatchmakerCommandModel matchmakerCommand) {
        log.debug("Handle matchmaker command, {}", matchmakerCommand);

        final var body = (KickClientMatchmakerCommandBodyModel) matchmakerCommand.getBody();
        final var clientId = body.getClientId();
        final var matchId = body.getMatchId();

        // Step 1. Finding client's match clients and adding for removing

        final var kickedMatchClients = currentState.getClients().stream()
                .filter(matchClient -> matchClient.getClientId().equals(clientId))
                .filter(matchClient -> matchClient.getMatchId().equals(matchId))
                .toList();

        changeOfState.getClientsToDelete().addAll(kickedMatchClients);

        log.info(
                "Client was kicked from matchmaker, " +
                        "clientId={}, " +
                        "matchmakerId={}, " +
                        "kickedMatchClients={}, " +
                        "matchmakerCommandId={}",
                clientId,
                matchmakerCommand.getMatchmakerId(),
                kickedMatchClients.size(),
                matchmakerCommand.getId());
    }
}
