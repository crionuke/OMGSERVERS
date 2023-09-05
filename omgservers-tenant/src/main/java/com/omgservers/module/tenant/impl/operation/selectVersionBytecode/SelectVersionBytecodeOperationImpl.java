package com.omgservers.module.tenant.impl.operation.selectVersionBytecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.exception.ServerSideConflictException;
import com.omgservers.exception.ServerSideInternalException;
import com.omgservers.exception.ServerSideNotFoundException;
import com.omgservers.model.version.VersionBytecodeModel;
import com.omgservers.operation.prepareShardSql.PrepareShardSqlOperation;
import com.omgservers.operation.transformPgException.TransformPgExceptionOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectVersionBytecodeOperationImpl implements SelectVersionBytecodeOperation {

    static private final String SQL = """
            select bytecode
            from $schema.tab_tenant_version
            where id = $1
            limit 1
            """;

    final TransformPgExceptionOperation transformPgExceptionOperation;
    final PrepareShardSqlOperation prepareShardSqlOperation;
    final ObjectMapper objectMapper;

    @Override
    public Uni<VersionBytecodeModel> selectVersionBytecode(final SqlConnection sqlConnection,
                                                           final int shard,
                                                           final Long versionId) {
        if (sqlConnection == null) {
            throw new ServerSideBadRequestException("sqlConnection is null");
        }
        if (versionId == null) {
            throw new ServerSideBadRequestException("id is null");
        }

        String preparedSql = prepareShardSqlOperation.prepareShardSql(SQL, shard);

        return sqlConnection.preparedQuery(preparedSql)
                .execute(Tuple.of(versionId))
                .map(RowSet::iterator)
                .map(iterator -> {
                    if (iterator.hasNext()) {
                        try {
                            log.info("Version was found, versionId={}", versionId);
                            final var row = iterator.next();
                            final var bytecode = objectMapper.readValue(row.getString("bytecode"),
                                    VersionBytecodeModel.class);
                            return bytecode;
                        } catch (IOException e) {
                            throw new ServerSideConflictException("bytecode can't be parsed, versionId=" + versionId);
                        }
                    } else {
                        throw new ServerSideNotFoundException(String.format("version was not found, " +
                                "versionId=%s", versionId));
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> transformPgExceptionOperation.transformPgException((PgException) t));
    }
}
