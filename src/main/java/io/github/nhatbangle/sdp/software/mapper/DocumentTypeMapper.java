package io.github.nhatbangle.sdp.software.mapper;

import io.github.nhatbangle.sdp.software.dto.document.DocumentTypeResponse;
import io.github.nhatbangle.sdp.software.entity.DocumentType;
import io.github.nhatbangle.sdp.software.projection.DocumentTypeInfo;

public class DocumentTypeMapper {

    public DocumentTypeResponse toResponse(DocumentTypeInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new DocumentTypeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public DocumentTypeResponse toResponse(DocumentType entity) {
        var updatedAt = entity.getUpdatedAt();
        return new DocumentTypeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

}
