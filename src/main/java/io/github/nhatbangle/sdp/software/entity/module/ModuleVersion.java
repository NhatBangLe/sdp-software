package io.github.nhatbangle.sdp.software.entity.module;

import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasModuleVersion;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "module_version")
@EntityListeners(AuditingEntityListener.class)
public class ModuleVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotBlank
    @Size(max = 150)
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Nullable
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Nullable
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MODULE_id", nullable = false)
    private Module module;

    @Nullable
    @OneToMany(mappedBy = "version", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<DeploymentProcessHasModuleVersion> usedByProcesses;

    @Nullable
    @OneToMany(mappedBy = "version", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ModuleDocument> documents;
}