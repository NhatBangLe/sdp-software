package io.github.nhatbangle.sdp.software.dto.software;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.software.Software}
 */
public record SoftwareResponse(
        String id,
        String name,
        String description,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}