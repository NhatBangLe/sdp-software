package io.github.nhatbangle.sdp.software.projection;

import io.github.nhatbangle.sdp.software.constant.MailTemplateType;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.MailTemplate}
 */
public interface MailTemplateInfo {
    String getId();

    String getSubject();

    byte[] getContent();

    MailTemplateType getType();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}