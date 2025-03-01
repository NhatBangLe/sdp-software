package io.github.nhatbangle.sdp.software.dto.document;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.module.ModuleDocument}
 */
public record ModuleDocumentCreateRequest(
        @Size(max = 150) @NotBlank String name,
        @Nullable @Size(max = 255) String description,
        @NotBlank @UUID String typeId,
        @Nullable List<@NotNull @UUID String> attachmentIds
        ) implements Serializable {
}