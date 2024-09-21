package com.omgservers.service.entrypoint.developer.impl.mappers;

import com.omgservers.schema.entrypoint.developer.dto.StageDto;
import com.omgservers.schema.model.tenantStage.TenantStageModel;
import org.mapstruct.Mapper;

@Mapper
public interface StageMapper {

    StageDto modelToDto(TenantStageModel model);
}
