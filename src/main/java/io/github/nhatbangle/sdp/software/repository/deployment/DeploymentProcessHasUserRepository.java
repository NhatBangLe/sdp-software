package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.composite.DeploymentProcessHasUserId;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasUser;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface DeploymentProcessHasUserRepository extends JpaRepository<DeploymentProcessHasUser, DeploymentProcessHasUserId> {

    List<DeploymentProcessHasUser> findAllById_ProcessId(@Min(0) @NotNull Long processId, @NotNull Sort sort);

    void deleteById_ProcessIdAndId_UserId(@Min(0) @NotNull Long processId, @NotNull @UUID String userId);

    Optional<DeploymentProcessHasUser> findById_ProcessIdAndId_UserId(@Min(0) @NotNull Long processId, @UUID @NotNull String userId);

}