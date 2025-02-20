package io.github.nhatbangle.sdp.software.dto.document;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument}
 */
public record SoftwareDocumentUpdateRequest(
        @Size(max = 150) @NotBlank String name,
        @Nullable @Size(max = 255) String description,
        @Nullable List<@UUID @NotNull String> attachmentIds
) implements Serializable {
}