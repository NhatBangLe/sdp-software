package io.github.nhatbangle.sdp.software.mapper;

import io.github.nhatbangle.sdp.software.dto.mail.MailTemplateResponse;
import io.github.nhatbangle.sdp.software.entity.MailTemplate;
import io.github.nhatbangle.sdp.software.projection.MailTemplateInfo;

import java.nio.charset.StandardCharsets;

public class MailTemplateMapper {

    public MailTemplateResponse toResponse(MailTemplateInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new MailTemplateResponse(
                entity.getId(),
                new String(entity.getContent(), StandardCharsets.UTF_8),
                entity.getType(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public MailTemplateResponse toResponse(MailTemplate entity) {
        var updatedAt = entity.getUpdatedAt();
        return new MailTemplateResponse(
                entity.getId(),
                new String(entity.getContent(), StandardCharsets.UTF_8),
                entity.getType(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

}
