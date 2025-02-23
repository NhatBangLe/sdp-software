package io.github.nhatbangle.sdp.software.dto.mail;

import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.MailTemplate}
 */
public record MailTemplateCreateRequest(
        @NotNull @Size(max = 5000) String content,
        @NotNull MailTemplateType type
) implements Serializable {
}