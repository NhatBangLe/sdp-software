package io.github.nhatbangle.sdp.software.dto.document;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.module.ModuleDocument}
 */
public record ModuleDocumentResponse(
        String id,
        String name,
        String description,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}