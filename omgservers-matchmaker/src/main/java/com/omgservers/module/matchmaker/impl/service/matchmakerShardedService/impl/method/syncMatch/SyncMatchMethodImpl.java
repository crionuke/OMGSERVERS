package com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.syncMatch;

import com.omgservers.dto.matchmaker.SyncMatchShardedRequest;
import com.omgservers.dto.matchmaker.SyncMatchShardedResponse;
import com.omgservers.model.match.MatchModel;
import com.omgservers.model.shard.ShardModel;
import com.omgservers.module.internal.InternalModule;
import com.omgservers.module.internal.factory.EventModelFactory;
import com.omgservers.module.internal.factory.LogModelFactory;
import com.omgservers.module.matchmaker.impl.operation.upsertMatch.UpsertMatchOperation;
import com.omgservers.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncMatchMethodImpl implements SyncMatchMethod {

    final InternalModule internalModule;

    final ChangeWithContextOperation changeWithContextOperation;
    final UpsertMatchOperation upsertMatchOperation;
    final CheckShardOperation checkShardOperation;

    final EventModelFactory eventModelFactory;
    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<SyncMatchShardedResponse> syncMatch(SyncMatchShardedRequest request) {
        SyncMatchShardedRequest.validate(request);

        final var match = request.getMatch();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeFunction(shardModel, match))
                .map(SyncMatchShardedResponse::new);
    }

    Uni<Boolean> changeFunction(ShardModel shardModel, MatchModel match) {
        return changeWithContextOperation.changeWithContext((context, sqlConnection) ->
                upsertMatchOperation.upsertMatch(context, sqlConnection, shardModel.shard(), match));
    }
}
