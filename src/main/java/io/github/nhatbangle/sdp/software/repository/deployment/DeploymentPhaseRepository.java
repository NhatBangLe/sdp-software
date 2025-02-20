package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentPhaseInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface DeploymentPhaseRepository extends JpaRepository<DeploymentPhase, String> {

    List<DeploymentPhaseInfo> findAllByProcess_Id(@Min(0) @NotNull Long id);

    Optional<DeploymentPhaseInfo> findInfoById(@UUID @NotNull String id);

}