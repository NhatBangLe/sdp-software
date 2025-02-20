package io.github.nhatbangle.sdp.software.mapper.software;

import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentResponse;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareDocumentInfo;

public class SoftwareDocumentMapper {

    public SoftwareDocumentResponse toResponse(SoftwareDocumentInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        var type = entity.getType();
        var version = entity.getVersion();

        return new SoftwareDocumentResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                new SoftwareDocumentResponse.DocumentType(type.getName()),
                new SoftwareDocumentResponse.SoftwareVersion(version.getName())
        );
    }

    public SoftwareDocumentResponse toResponse(SoftwareDocument entity) {
        var type = entity.getType();
        var version = entity.getVersion();
        var updatedAt = entity.getUpdatedAt();

        return new SoftwareDocumentResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                new SoftwareDocumentResponse.DocumentType(type.getName()),
                new SoftwareDocumentResponse.SoftwareVersion(version.getName())
        );
    }

}
