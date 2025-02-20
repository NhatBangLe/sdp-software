package io.github.nhatbangle.sdp.software.dto.document;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument}
 */
public record SoftwareDocumentResponse(
        String id,
        String name,
        String description,
        long createdAtMs,
        @Nullable Long updatedAtMs,
        DocumentType type,
        SoftwareVersion version
) implements Serializable {
    public record DocumentType(
            String name
    ) implements Serializable {
    }

    public record SoftwareVersion(
            String name
    ) implements Serializable {
    }
}