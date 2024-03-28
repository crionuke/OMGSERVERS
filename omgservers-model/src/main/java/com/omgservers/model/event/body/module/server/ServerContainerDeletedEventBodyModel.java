package com.omgservers.model.event.body.module.server;

import com.omgservers.model.event.EventBodyModel;
import com.omgservers.model.event.EventQualifierEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ServerContainerDeletedEventBodyModel extends EventBodyModel {

    @NotNull
    Long serverId;

    @NotNull
    Long id;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.SERVER_CONTAINER_DELETED;
    }
}
