package com.omgservers.service.module.tenant.impl.operation.selectActiveVersionMatchmakerRequestsByVersionId;

import com.omgservers.model.versionMatchmakerRequest.VersionMatchmakerRequestModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.util.List;

public interface SelectActiveVersionMatchmakerRequestsByVersionId {
    Uni<List<VersionMatchmakerRequestModel>> selectActiveVersionMatchmakerRequestsByVersionId(SqlConnection sqlConnection,
                                                                                              int shard,
                                                                                              Long tenantId,
                                                                                              Long versionId);
}
