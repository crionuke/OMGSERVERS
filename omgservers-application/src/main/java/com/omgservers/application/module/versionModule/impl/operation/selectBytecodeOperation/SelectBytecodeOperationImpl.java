package com.omgservers.application.module.versionModule.impl.operation.selectBytecodeOperation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.application.operation.prepareShardSqlOperation.PrepareShardSqlOperation;
import com.omgservers.application.module.versionModule.model.VersionBytecodeModel;
import com.omgservers.application.exception.ServerSideBadRequestException;
import com.omgservers.application.exception.ServerSideConflictException;
import com.omgservers.application.exception.ServerSideInternalException;
import com.omgservers.application.exception.ServerSideNotFoundException;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectBytecodeOperationImpl implements SelectBytecodeOperation {

    static private final String sql = """
            select bytecode
            from $schema.tab_version
            where id = $1
            limit 1
            """;

    final PrepareShardSqlOperation prepareShardSqlOperation;
    final ObjectMapper objectMapper;

    @Override
    public Uni<VersionBytecodeModel> selectBytecode(final SqlConnection sqlConnection,
                                                    final int shard,
                                                    final Long versionId) {
        if (sqlConnection == null) {
            throw new ServerSideBadRequestException("sqlConnection is null");
        }
        if (versionId == null) {
            throw new ServerSideBadRequestException("id is null");
        }

        String preparedSql = prepareShardSqlOperation.prepareShardSql(sql, shard);

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
                            throw new ServerSideInternalException("bytecode can't be parsed, versionId=" + versionId);
                        }
                    } else {
                        throw new ServerSideNotFoundException(String.format("version was not found, " +
                                "versionId=%s", versionId));
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> new ServerSideConflictException(String
                        .format("unhandled PgException, %s", t.getMessage())));
    }
}
