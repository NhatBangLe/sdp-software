package io.github.nhatbangle.sdp.software.entity.deployment;

import io.github.nhatbangle.sdp.software.entity.module.ModuleVersion;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentProcessHasModuleVersionId;
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
@Table(name = "deployment_process_has_module_version")
@EntityListeners(AuditingEntityListener.class)
public class DeploymentProcessHasModuleVersion {
    @EmbeddedId
    private DeploymentProcessHasModuleVersionId id;

    @MapsId("processId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROCESS_id", nullable = false)
    private DeploymentProcess process;

    @MapsId("versionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "VERSION_id", nullable = false)
    private ModuleVersion version;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;
}