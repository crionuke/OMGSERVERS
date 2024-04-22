package com.omgservers.service.integration.jenkins.impl.operation.getJenkinsClient;

import com.omgservers.service.integration.jenkins.impl.operation.getJenkinsClient.dto.getJobByBuildNumber.GetJobByBuildNumberResponse;
import com.omgservers.service.integration.jenkins.impl.operation.getJenkinsClient.dto.getQueueItemResponse.GetQueueItemResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

public interface JenkinsApi {

    @GET
    @Path("/queue/item/{itemNumber}/api/json")
    Uni<GetQueueItemResponse> getQueueItem(@PathParam("itemNumber") Integer itemNumber);

    @POST
    @Path("/job/luajit-worker-builder-v1/buildWithParameters")
    Uni<Response> runLuaJitWorkerBuilderV1(@FormParam("groupId") String groupId,
                                           @FormParam("containerName") String containerName,
                                           @FormParam("versionId") String versionId,
                                           @FormParam("sourceCodeJson") String sourceCodeJson);

    @GET
    @Path("/job/luajit-worker-builder-v1/{buildNumber}/api/json")
    Uni<GetJobByBuildNumberResponse> getLuaJitWorkerBuilderV1(@PathParam("buildNumber") Integer buildNumber);

    @GET
    @Path("/job/luajit-worker-builder-v1/{buildNumber}/artifact/image")
    Uni<String> getLuaJitWorkerBuilderV1ImageArtifact(@PathParam("buildNumber") Integer buildNumber);
}
