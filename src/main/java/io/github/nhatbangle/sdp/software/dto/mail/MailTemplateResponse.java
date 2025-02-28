package io.github.nhatbangle.sdp.software.dto.mail;

import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.MailTemplate}
 */
public record MailTemplateResponse(
        String id,
        String subject,
        String content,
        MailTemplateType type,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}