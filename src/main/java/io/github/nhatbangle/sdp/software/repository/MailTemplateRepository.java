package io.github.nhatbangle.sdp.software.repository;

import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import io.github.nhatbangle.sdp.software.entity.MailTemplate;
import io.github.nhatbangle.sdp.software.projection.MailTemplateInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface MailTemplateRepository extends JpaRepository<MailTemplate, String> {
    Optional<MailTemplateInfo> findInfoById(@NotNull @UUID String id);

    Optional<MailTemplate> findByUser_IdAndType(@NotNull @UUID String id, @NotNull MailTemplateType type);

}