package com.omgservers.model.dto.tenant;

import com.omgservers.model.versionLobbyRequest.VersionLobbyRequestModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetVersionLobbyRequestResponse {

    VersionLobbyRequestModel versionLobbyRequest;
}
