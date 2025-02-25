package io.github.nhatbangle.sdp.software.repository.software;

import io.github.nhatbangle.sdp.software.entity.composite.SoftwareDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocumentHasAttachment;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Stream;

@Validated
public interface SoftwareDocumentHasAttachmentRepository extends JpaRepository<SoftwareDocumentHasAttachment, SoftwareDocumentHasAttachmentId> {
    boolean existsById_DocumentIdAndId_AttachmentId(@UUID @NotNull String documentId, @UUID @NotNull String attachmentId);

    Stream<SoftwareDocumentHasAttachment> findAllById_DocumentId(@NotNull @UUID String documentId, @NotNull Sort sort);

}