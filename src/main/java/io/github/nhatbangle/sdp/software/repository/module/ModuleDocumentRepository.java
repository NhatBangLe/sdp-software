package io.github.nhatbangle.sdp.software.repository.module;

import io.github.nhatbangle.sdp.software.entity.module.ModuleDocument;
import io.github.nhatbangle.sdp.software.projection.module.ModuleDocumentInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface ModuleDocumentRepository extends JpaRepository<ModuleDocument, String> {

    Page<ModuleDocumentInfo> findAllByVersion_IdAndType_NameContainsIgnoreCaseAndNameContainsIgnoreCase(
            @UUID @NotNull String moduleVersionId,
            @NotNull String documentTypeName,
            @NotNull String documentName,
            @NotNull Pageable pageable
    );

    Optional<ModuleDocumentInfo> findInfoById(@UUID @NotNull String id);

}