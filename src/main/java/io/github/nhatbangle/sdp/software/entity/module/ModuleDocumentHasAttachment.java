package io.github.nhatbangle.sdp.software.entity.module;

import io.github.nhatbangle.sdp.software.entity.Attachment;
import io.github.nhatbangle.sdp.software.entity.composite.ModuleDocumentHasAttachmentId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "module_document_has_attachment")
@EntityListeners(AuditingEntityListener.class)
public class ModuleDocumentHasAttachment {
    @EmbeddedId
    private ModuleDocumentHasAttachmentId id;

    @MapsId("documentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DOCUMENT_id", nullable = false)
    private ModuleDocument document;

    @MapsId("attachmentId")
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(name = "ATTACHMENT_id", nullable = false)
    private Attachment attachment;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}