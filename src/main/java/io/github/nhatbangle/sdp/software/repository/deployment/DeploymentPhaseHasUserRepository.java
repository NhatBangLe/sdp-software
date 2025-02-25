package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.composite.DeploymentPhaseHasUserId;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseHasUser;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Stream;

@Validated
public interface DeploymentPhaseHasUserRepository extends JpaRepository<DeploymentPhaseHasUser, DeploymentPhaseHasUserId> {

    void deleteById_PhaseIdAndId_UserId(@NotNull @UUID String phaseId, @NotNull @UUID String userId);

    Stream<DeploymentPhaseHasUser> findAllById_PhaseId(@NotNull @UUID String phaseId, @NotNull Sort sort);

}