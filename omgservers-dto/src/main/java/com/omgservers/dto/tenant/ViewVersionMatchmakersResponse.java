package com.omgservers.dto.tenant;

import com.omgservers.model.versionMatchmaker.VersionMatchmakerModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewVersionMatchmakersResponse {

    List<VersionMatchmakerModel> versionMatchmakers;
}
