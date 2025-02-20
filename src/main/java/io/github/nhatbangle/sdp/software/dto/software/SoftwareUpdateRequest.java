package io.github.nhatbangle.sdp.software.dto.software;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.software.Software}
 */
public record SoftwareUpdateRequest(
        @Size(max = 150) @NotBlank String name,
        @Nullable @Size(max = 255) String description
) implements Serializable {
}