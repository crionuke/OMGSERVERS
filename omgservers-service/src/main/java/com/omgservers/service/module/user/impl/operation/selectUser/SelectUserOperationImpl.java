package com.omgservers.service.module.user.impl.operation.selectUser;

import com.omgservers.model.user.UserModel;
import com.omgservers.service.module.user.impl.mapper.UserModelMapper;
import com.omgservers.service.operation.selectObject.SelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectUserOperationImpl implements SelectUserOperation {

    final SelectObjectOperation selectObjectOperation;

    final UserModelMapper userModelMapper;

    @Override
    public Uni<UserModel> selectUser(final SqlConnection sqlConnection,
                                     final int shard,
                                     final Long id,
                                     final Boolean deleted) {
        return selectObjectOperation.selectObject(
                sqlConnection,
                shard,
                """
                        select id, created, modified, role, password_hash, deleted
                        from $schema.tab_user
                        where id = $1 and deleted = $2
                        limit 1
                        """,
                Arrays.asList(
                        id,
                        deleted
                ),
                "User",
                userModelMapper::fromRow);
    }
}
