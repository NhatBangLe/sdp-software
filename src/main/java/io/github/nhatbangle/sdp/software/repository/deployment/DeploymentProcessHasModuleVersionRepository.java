package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.composite.DeploymentProcessHasModuleVersionId;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasModuleVersion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Stream;

@Validated
public interface DeploymentProcessHasModuleVersionRepository extends JpaRepository<DeploymentProcessHasModuleVersion, DeploymentProcessHasModuleVersionId> {
    Stream<DeploymentProcessHasModuleVersion> findAllById_ProcessId(@NotNull @Min(0) Long processId, @NotNull Sort sort);
}