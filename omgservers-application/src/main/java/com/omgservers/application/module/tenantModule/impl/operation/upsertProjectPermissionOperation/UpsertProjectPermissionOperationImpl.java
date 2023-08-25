package com.omgservers.application.module.tenantModule.impl.operation.upsertProjectPermissionOperation;

import com.omgservers.base.impl.operation.prepareShardSqlOperation.PrepareShardSqlOperation;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.exception.ServerSideConflictException;
import com.omgservers.exception.ServerSideNotFoundException;
import com.omgservers.model.projectPermission.ProjectPermissionModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneOffset;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertProjectPermissionOperationImpl implements UpsertProjectPermissionOperation {

    static private final String sql = """
            insert into $schema.tab_tenant_project_permission(id, project_id, created, user_id, permission)
            values($1, $2, $3, $4, $5)
            on conflict (project_id, user_id, permission) do
            nothing
            """;

    final PrepareShardSqlOperation prepareShardSqlOperation;

    @Override
    public Uni<Boolean> upsertProjectPermission(final SqlConnection sqlConnection,
                                                final int shard,
                                                final ProjectPermissionModel permission) {
        if (sqlConnection == null) {
            throw new ServerSideBadRequestException("sqlConnection is null");
        }
        if (permission == null) {
            throw new ServerSideBadRequestException("permission is null");
        }

        return upsertQuery(sqlConnection, shard, permission)
                .invoke(inserted -> {
                    if (inserted) {
                        log.info("Project permission was inserted, permission={}", permission);
                    } else {
                        log.info("Project permission was updated, permission={}", permission);
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> {
                    final var pgException = (PgException) t;
                    final var code = pgException.getSqlState();
                    final var column = pgException.getColumn();
                    if (code.equals("23503")) {
                        // foreign_key_violation
                        return new ServerSideNotFoundException("project was not found, permission=" + permission);
                    } else {
                        return new ServerSideConflictException("unhandled PgException, " + t.getMessage());
                    }
                });
    }

    Uni<Boolean> upsertQuery(SqlConnection sqlConnection, int shard, ProjectPermissionModel permission) {
        var preparedSql = prepareShardSqlOperation.prepareShardSql(sql, shard);
        return sqlConnection.preparedQuery(preparedSql)
                .execute(Tuple.of(
                        permission.getId(),
                        permission.getProjectId(),
                        permission.getCreated().atOffset(ZoneOffset.UTC),
                        permission.getUserId(),
                        permission.getPermission()))
                .map(rowSet -> rowSet.rowCount() > 0);
    }
}
