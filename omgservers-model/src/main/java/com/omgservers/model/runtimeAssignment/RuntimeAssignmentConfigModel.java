package com.omgservers.model.runtimeAssignment;

import com.omgservers.model.matchmakerMatchClient.MatchmakerMatchClientModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuntimeAssignmentConfigModel {

    static public RuntimeAssignmentConfigModel create() {
        final var runtimeAssignmentConfig = new RuntimeAssignmentConfigModel();
        return runtimeAssignmentConfig;
    }

    MatchmakerMatchClientModel matchClient;
}
