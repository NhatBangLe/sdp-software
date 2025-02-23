package io.github.nhatbangle.sdp.software.repository;

import io.github.nhatbangle.sdp.software.entity.composite.ModuleDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.module.ModuleDocumentHasAttachment;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ModuleDocumentHasAttachmentRepository extends JpaRepository<ModuleDocumentHasAttachment, ModuleDocumentHasAttachmentId> {
    boolean existsById_DocumentIdAndId_AttachmentId(@UUID @NotNull String documentId, @UUID @NotNull String attachmentId);

}