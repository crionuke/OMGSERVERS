package com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.response;

import com.omgservers.application.module.matchmakerModule.model.matchmaker.MatchmakerModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMatchmakerInternalResponse {

    MatchmakerModel matchmaker;
}
