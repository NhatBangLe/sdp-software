package io.github.nhatbangle.sdp.software.dto.customer;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.Customer}
 */
public record CustomerUpdateRequest(
        @Size(max = 150) @NotBlank String name,
        @Nullable @Size(max = 100) @NotBlank String email
) implements Serializable {
}