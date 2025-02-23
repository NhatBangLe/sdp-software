package io.github.nhatbangle.sdp.software.dto.mail;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.MailTemplate}
 */
public record MailSendPayload(
        byte[] content,
        String charset,
        String email
) implements Serializable {
}