package io.github.nhatbangle.sdp.software.dto.customer;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.Customer}
 */
public record CustomerResponse(
        String id,
        String name,
        String email,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}