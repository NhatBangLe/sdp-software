package io.github.nhatbangle.sdp.software.dto.notification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.MailTemplate}
 */
public record MailSendPayload(
        @NotNull String subject,
        @NotNull byte[] content,
        @NotNull String charset,
        @Email String toEmail
) implements Serializable {
}