package io.github.nhatbangle.sdp.software.mapper.module;

import io.github.nhatbangle.sdp.software.dto.module.ModuleResponse;
import io.github.nhatbangle.sdp.software.entity.module.Module;
import io.github.nhatbangle.sdp.software.projection.module.ModuleInfo;

public class ModuleDocumentMapper {

    public ModuleResponse toResponse(ModuleInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new ModuleResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public ModuleResponse toResponse(Module entity) {
        var updatedAt = entity.getUpdatedAt();
        return new ModuleResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

}
