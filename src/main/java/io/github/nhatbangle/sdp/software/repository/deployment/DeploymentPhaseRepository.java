package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentPhaseInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.stream.Stream;

@Validated
public interface DeploymentPhaseRepository extends JpaRepository<DeploymentPhase, String> {

    Stream<DeploymentPhaseInfo> findAllInfoByProcess_Id(@Min(0) @NotNull Long id, Sort sort);

    Optional<DeploymentPhaseInfo> findInfoById(@UUID @NotNull String id);

}