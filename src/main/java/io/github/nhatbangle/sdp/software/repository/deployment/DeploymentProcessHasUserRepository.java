package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentProcessHasUserId;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasUser;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.stream.Stream;

@Validated
public interface DeploymentProcessHasUserRepository extends JpaRepository<DeploymentProcessHasUser, DeploymentProcessHasUserId> {

    Stream<DeploymentProcessHasUser> findAllById_ProcessId(@Min(0) @NotNull Long processId, @NotNull Sort sort);

    void deleteById_ProcessIdAndId_UserId(@Min(0) @NotNull Long processId, @NotNull @UUID String userId);

    Optional<DeploymentProcessHasUser> findById_ProcessIdAndId_UserId(@Min(0) @NotNull Long processId, @UUID @NotNull String userId);

    @Query("""
            SELECT new io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessInfo(
                d.id.processId, d.process.status, d.createdAt, d.process.updatedAt,
                d.process.softwareVersion.software.name, d.process.softwareVersion.name, d.process.customer.name)
            FROM DeploymentProcessHasUser d
            WHERE d.id.userId = :memberId
                    AND upper(d.process.softwareVersion.software.name) LIKE upper(concat('%', :softwareName, '%'))
                    AND upper(d.process.customer.name) LIKE upper(concat('%', :customerName, '%'))
                    AND (:status is null OR d.process.status = :status)
            """)
    Page<DeploymentProcessInfo> findAllByMember_IdAndSoftwareVersion_Software_NameContainsIgnoreCaseAndCustomer_NameContainsIgnoreCaseAndStatus(
            @NotNull @UUID @Param("memberId") String memberId,
            @NotNull @Param("softwareName") String softwareName,
            @NotNull @Param("customerName") String customerName,
            @Nullable @Param("status") DeploymentProcessStatus status,
            @NotNull Pageable pageable
    );

}