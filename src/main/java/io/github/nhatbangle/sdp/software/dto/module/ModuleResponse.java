package io.github.nhatbangle.sdp.software.dto.module;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.module.Module}
 */
public record ModuleResponse(
        String id,
        String name,
        String description,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}