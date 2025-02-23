package io.github.nhatbangle.sdp.software.repository;

import io.github.nhatbangle.sdp.software.entity.composite.SoftwareDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocumentHasAttachment;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface SoftwareDocumentHasAttachmentRepository extends JpaRepository<SoftwareDocumentHasAttachment, SoftwareDocumentHasAttachmentId> {
    boolean existsById_DocumentIdAndId_AttachmentId(@UUID @NotNull String documentId, @UUID @NotNull String attachmentId);
}