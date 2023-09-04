package com.omgservers;

import com.omgservers.dto.ShardedRequest;
import com.omgservers.model.shard.ShardModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequest {

    public static void validate(ChangeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    ShardedRequest request;
    BiFunction<SqlConnection, ShardModel, Uni<Boolean>> changeFunction;
}
