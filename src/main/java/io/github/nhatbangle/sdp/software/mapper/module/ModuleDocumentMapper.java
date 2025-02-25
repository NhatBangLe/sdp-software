package io.github.nhatbangle.sdp.software.mapper.module;

import io.github.nhatbangle.sdp.software.dto.document.ModuleDocumentResponse;
import io.github.nhatbangle.sdp.software.entity.module.ModuleDocument;
import io.github.nhatbangle.sdp.software.projection.module.ModuleDocumentInfo;

public class ModuleDocumentMapper {

    public ModuleDocumentResponse toResponse(ModuleDocumentInfo entity) {
        var type = entity.getType();
        var updatedAt = entity.getUpdatedAt();
        var version = entity.getVersion();

        return new ModuleDocumentResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                new ModuleDocumentResponse.DocumentType(type.getName()),
                new ModuleDocumentResponse.ModuleVersion(version.getName())
        );
    }

    public ModuleDocumentResponse toResponse(ModuleDocument entity) {
        var updatedAt = entity.getUpdatedAt();
        var type = entity.getType();
        var version = entity.getVersion();

        return new ModuleDocumentResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                new ModuleDocumentResponse.DocumentType(type.getName()),
                new ModuleDocumentResponse.ModuleVersion(version.getName())
        );
    }

}
