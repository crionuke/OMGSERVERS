package com.omgservers.model.event.body;

import com.omgservers.model.event.EventBodyModel;
import com.omgservers.model.event.EventQualifierEnum;
import com.omgservers.model.player.PlayerAttributesModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SetAttributesRequestedEventBodyModel extends EventBodyModel {

    @NotNull
    Long runtimeId;

    @NotNull
    Long userId;

    @NotNull
    Long clientId;

    @NotNull
    PlayerAttributesModel attributes;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.SET_ATTRIBUTES_REQUESTED;
    }

    @Override
    public Long getGroupId() {
        return runtimeId;
    }
}
