package io.github.nhatbangle.sdp.software.dto.module;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.module.Module}
 */
public record ModuleUpdateRequest(
        @Size(max = 150) @NotBlank String name,
        @Nullable @Size(max = 255) String description
) implements Serializable {
}