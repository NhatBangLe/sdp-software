package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.composite.DeploymentPhaseHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseHasAttachment;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface DeploymentPhaseHasAttachmentRepository extends JpaRepository<DeploymentPhaseHasAttachment, DeploymentPhaseHasAttachmentId> {

    boolean existsById_DeploymentPhaseIdAndId_AttachmentId(
            @UUID @NotNull String phaseId, @UUID @NotNull String attachmentId);

}