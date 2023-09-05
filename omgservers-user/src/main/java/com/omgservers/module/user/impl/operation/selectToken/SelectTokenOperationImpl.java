package com.omgservers.module.user.impl.operation.selectToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.exception.ServerSideNotFoundException;
import com.omgservers.model.token.TokenModel;
import com.omgservers.operation.prepareShardSql.PrepareShardSqlOperation;
import com.omgservers.operation.transformPgException.TransformPgExceptionOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectTokenOperationImpl implements SelectTokenOperation {

    static private final String SQL = """
            select id, user_id, created, expire, hash
            from $schema.tab_user_token
            where id = $1
            limit 1
            """;

    final TransformPgExceptionOperation transformPgExceptionOperation;
    final PrepareShardSqlOperation prepareShardSqlOperation;
    final ObjectMapper objectMapper;

    @Override
    public Uni<TokenModel> selectToken(final SqlConnection sqlConnection,
                                       final int shard,
                                       final Long tokenId) {
        if (sqlConnection == null) {
            throw new ServerSideBadRequestException("sqlConnection is null");
        }
        if (tokenId == null) {
            throw new ServerSideBadRequestException("tokenId is null");
        }

        String preparedSql = prepareShardSqlOperation.prepareShardSql(SQL, shard);

        return sqlConnection.preparedQuery(preparedSql)
                .execute(Tuple.of(tokenId))
                .map(RowSet::iterator)
                .map(iterator -> {
                    if (iterator.hasNext()) {
                        log.info("Token was found, uuid={}, shard={}", tokenId, shard);
                        return createToken(iterator.next());
                    } else {
                        throw new ServerSideNotFoundException(String.format("token was not found, tokenId=%s",
                                tokenId));
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> transformPgExceptionOperation.transformPgException((PgException) t));
    }

    TokenModel createToken(Row row) {
        TokenModel tokenModel = new TokenModel();
        tokenModel.setId(row.getLong("id"));
        tokenModel.setUserId(row.getLong("user_id"));
        tokenModel.setCreated(row.getOffsetDateTime("created").toInstant());
        tokenModel.setExpire(row.getOffsetDateTime("expire").toInstant());
        tokenModel.setHash(row.getString("hash"));
        return tokenModel;
    }
}
