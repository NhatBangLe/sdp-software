package io.github.nhatbangle.sdp.software.mapper.module;

import io.github.nhatbangle.sdp.software.dto.module.ModuleVersionResponse;
import io.github.nhatbangle.sdp.software.entity.module.ModuleVersion;
import io.github.nhatbangle.sdp.software.projection.module.ModuleVersionInfo;

public class ModuleVersionMapper {

    public ModuleVersionResponse toResponse(ModuleVersionInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new ModuleVersionResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public ModuleVersionResponse toResponse(ModuleVersion entity) {
        var updatedAt = entity.getUpdatedAt();
        return new ModuleVersionResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

}
