package com.omgservers.module.matchmaker.impl.operation.deleteMatchmaker;

import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.model.event.body.MatchmakerDeletedEventBodyModel;
import com.omgservers.module.internal.factory.EventModelFactory;
import com.omgservers.module.internal.factory.LogModelFactory;
import com.omgservers.module.internal.impl.operation.upsertEvent.UpsertEventOperation;
import com.omgservers.module.internal.impl.operation.upsertLog.UpsertLogOperation;
import com.omgservers.operation.prepareShardSql.PrepareShardSqlOperation;
import com.omgservers.operation.transformPgException.TransformPgExceptionOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteMatchmakerOperationImpl implements DeleteMatchmakerOperation {

    static private final String sql = """
            delete from $schema.tab_matchmaker
            where id = $1
            """;

    final TransformPgExceptionOperation transformPgExceptionOperation;
    final PrepareShardSqlOperation prepareShardSqlOperation;
    final UpsertEventOperation upsertEventOperation;
    final UpsertLogOperation upsertLogOperation;

    final EventModelFactory eventModelFactory;
    final LogModelFactory logModelFactory;

    @Override
    public Uni<Boolean> deleteMatchmaker(final ChangeContext<?> changeContext,
                                         final SqlConnection sqlConnection,
                                         final int shard,
                                         final Long id) {
        if (changeContext == null) {
            throw new IllegalArgumentException("changeContext is null");
        }
        if (sqlConnection == null) {
            throw new IllegalArgumentException("sqlConnection is null");
        }
        if (id == null) {
            throw new IllegalArgumentException("uuid is null");
        }

        String preparedSql = prepareShardSqlOperation.prepareShardSql(sql, shard);
        return sqlConnection.preparedQuery(preparedSql)
                .execute(Tuple.of(id))
                .map(rowSet -> rowSet.rowCount() > 0)
                .call(objectWasDeleted -> upsertEvent(objectWasDeleted, changeContext, sqlConnection, id))
                .call(objectWasDeleted -> upsertLog(objectWasDeleted, changeContext, sqlConnection, id))
                .invoke(objectWasDeleted -> {
                    if (objectWasDeleted) {
                        log.info("Matchmaker was deleted, shard={}, id={}", shard, id);
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> transformPgExceptionOperation.transformPgException((PgException) t));
    }

    Uni<Boolean> upsertEvent(final boolean objectWasDeleted,
                             final ChangeContext<?> changeContext,
                             final SqlConnection sqlConnection,
                             final Long id) {
        if (objectWasDeleted) {
            final var body = new MatchmakerDeletedEventBodyModel(id);
            final var event = eventModelFactory.create(body);
            return upsertEventOperation.upsertEvent(changeContext, sqlConnection, event);
        } else {
            return Uni.createFrom().item(false);
        }
    }

    Uni<Boolean> upsertLog(final boolean objectWasDeleted,
                           final ChangeContext<?> changeContext,
                           final SqlConnection sqlConnection,
                           final Long id) {
        if (objectWasDeleted) {
            final var changeLog = logModelFactory.create("Matchmaker was deleted, id=" + id);
            return upsertLogOperation.upsertLog(changeContext, sqlConnection, changeLog);
        } else {
            return Uni.createFrom().item(false);
        }
    }
}
