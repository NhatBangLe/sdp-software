package io.github.nhatbangle.sdp.software.dto.document;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.DocumentType}
 */
public record DocumentTypeResponse(
        String id,
        String name,
        String description,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}