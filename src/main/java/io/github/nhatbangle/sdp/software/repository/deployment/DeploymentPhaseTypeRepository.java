package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentPhaseTypeInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface DeploymentPhaseTypeRepository extends JpaRepository<DeploymentPhaseType, String> {

    Page<DeploymentPhaseTypeInfo> findAllByUser_IdAndNameContainsIgnoreCase(
            @NotNull @UUID String id,
            @NotNull String name,
            @NotNull Pageable pageable
    );

    Optional<DeploymentPhaseTypeInfo> findInfoById(@UUID @NotNull String id);

}