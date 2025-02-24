package io.github.nhatbangle.sdp.software.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.SoftwareLicense}
 */
public record SoftwareLicenseUpdateRequest(
        @Nullable @Size(max = 255) String description,
        @Min(0) @NotNull Integer expireAlertIntervalDay
) implements Serializable {
}