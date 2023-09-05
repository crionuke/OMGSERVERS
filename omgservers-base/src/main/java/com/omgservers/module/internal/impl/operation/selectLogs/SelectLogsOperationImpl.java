package com.omgservers.module.internal.impl.operation.selectLogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.model.log.LogModel;
import com.omgservers.operation.transformPgException.TransformPgExceptionOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectLogsOperationImpl implements SelectLogsOperation {

    static private final String SQL = """
            select id, created, message
            from internal.tab_log
            """;

    final TransformPgExceptionOperation transformPgExceptionOperation;

    final ObjectMapper objectMapper;

    @Override
    public Uni<List<LogModel>> selectLogs(SqlConnection sqlConnection) {
        if (sqlConnection == null) {
            throw new ServerSideBadRequestException("sqlConnection is null");
        }

        return sqlConnection.preparedQuery(SQL)
                .execute()
                .map(RowSet::iterator)
                .map(iterator -> {
                    final List<LogModel> results = new ArrayList<LogModel>();
                    while (iterator.hasNext()) {
                        final var log = createLog(iterator.next());
                        results.add(log);
                    }
                    if (results.size() > 0) {
                        log.info("Logs were found, size={}", results.size());
                    } else {
                        log.info("Logs were not found");
                    }
                    return results;
                })
                .onFailure(PgException.class)
                .transform(t -> transformPgExceptionOperation.transformPgException((PgException) t));
    }

    LogModel createLog(Row row) {
        final var log = new LogModel();
        log.setId(row.getLong("id"));
        log.setCreated(row.getOffsetDateTime("created").toInstant());
        log.setMessage(row.getString("message"));
        return log;
    }
}
