package io.github.nhatbangle.sdp.software.dto;

import io.github.nhatbangle.sdp.software.constant.AttachmentOperator;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument}
 */
public record AttachmentUpdateRequest(
        @UUID @NotNull String attachmentId,
        @NotNull AttachmentOperator operator
) implements Serializable {
}