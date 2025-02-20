package io.github.nhatbangle.sdp.software.repository.software;

import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareDocumentInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface SoftwareDocumentRepository extends JpaRepository<SoftwareDocument, String> {

    Page<SoftwareDocumentInfo> findAllByVersion_IdAndType_NameContainsIgnoreCaseAndNameContainsIgnoreCase(
            @UUID @NotNull String moduleVersionId,
            @NotNull String documentTypeName,
            @NotNull String documentName,
            @NotNull Pageable pageable
    );

    Optional<SoftwareDocumentInfo> findInfoById(@UUID @NotNull String documentId);

}