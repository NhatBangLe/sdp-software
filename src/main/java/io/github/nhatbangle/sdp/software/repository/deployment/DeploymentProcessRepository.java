package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface DeploymentProcessRepository extends JpaRepository<DeploymentProcess, Long> {

    @Query("""
            SELECT new io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessInfo(
                d.id, d.status, d.createdAt, d.updatedAt,
                d.softwareVersion.software.name, d.softwareVersion.name, d.customer.name)
            FROM DeploymentProcess d
            WHERE upper(d.softwareVersion.name) LIKE upper(concat('%', :versionName, '%'))
                    AND upper(d.customer.name) LIKE upper(concat('%', :customerName, '%'))
                    AND (:status is null OR d.status = :status)
            """)
    Page<DeploymentProcessInfo> findAllBySoftwareVersion_NameContainsIgnoreCaseAndCustomer_NameContainsIgnoreCaseAndStatus(
            @NotNull @Param("versionName") String softwareVersionName,
            @NotNull @Param("customerName") String customerName,
            @Nullable @Param("status") DeploymentProcessStatus status,
            @NotNull Pageable pageable
    );

    @Query("""
            SELECT new io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessInfo(
                d.id, d.status, d.createdAt, d.updatedAt,
                d.softwareVersion.software.name, d.softwareVersion.name, d.customer.name)
            FROM DeploymentProcess d WHERE d.id = ?1
            """)
    Optional<DeploymentProcessInfo> findInfoById(@Min(0) @NotNull Long id);

}