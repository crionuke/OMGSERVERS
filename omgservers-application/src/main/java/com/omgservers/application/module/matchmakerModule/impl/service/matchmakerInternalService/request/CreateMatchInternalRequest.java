package com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.request;

import com.omgservers.application.module.matchmakerModule.model.match.MatchModel;
import com.omgservers.application.request.InternalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchInternalRequest implements InternalRequest {

    static public void validate(CreateMatchInternalRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    MatchModel match;

    @Override
    public String getRequestShardKey() {
        return match.getMatchmakerId().toString();
    }
}
