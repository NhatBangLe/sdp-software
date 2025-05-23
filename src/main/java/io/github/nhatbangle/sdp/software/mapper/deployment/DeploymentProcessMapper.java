package io.github.nhatbangle.sdp.software.mapper.deployment;

import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessHasModuleVersionResponse;
import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessHasSoftwareVersionResponse;
import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessResponse;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareNameAndVersionResponse;
import io.github.nhatbangle.sdp.software.entity.Customer;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessHasModuleVersionInfo;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessHasSoftwareVersionInfo;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessInfo;

public class DeploymentProcessMapper {

    public DeploymentProcessResponse toResponse(DeploymentProcessInfo entity) {
        var updatedAt = entity.updatedAt();
        return new DeploymentProcessResponse(
                entity.id(),
                entity.status(),
                entity.createdAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                new DeploymentProcessResponse.Software(
                        entity.softwareName(),
                        entity.softwareVersionName()
                ),
                new DeploymentProcessResponse.Customer( entity.customerName() )
        );
    }

    public DeploymentProcessResponse toResponse(DeploymentProcess entity) {
        var updatedAt = entity.getUpdatedAt();
        return new DeploymentProcessResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                softwareVersionToResponse(entity.getSoftwareVersion()),
                customerToResponse(entity.getCustomer())
        );
    }

    private DeploymentProcessResponse.Software softwareVersionToResponse(SoftwareVersion version) {
        return new DeploymentProcessResponse.Software(
                version.getSoftware().getName(),
                version.getName()
        );
    }

    private DeploymentProcessResponse.Customer customerToResponse(Customer customer) {
        return new DeploymentProcessResponse.Customer(
                customer.getName()
        );
    }

    public DeploymentProcessHasModuleVersionResponse toResponse(DeploymentProcessHasModuleVersionInfo entity) {
        var versionInfo = entity.getVersion();
        var moduleInfo = versionInfo.getModule();
        var version = new DeploymentProcessHasModuleVersionResponse.ModuleVersion(versionInfo.getId(), versionInfo.getName());
        var module = new DeploymentProcessHasModuleVersionResponse.Module(moduleInfo.getId(), moduleInfo.getName());

        return new DeploymentProcessHasModuleVersionResponse(
                entity.getProcess().getId(),
                version,
                module
        );
    }

    public DeploymentProcessHasSoftwareVersionResponse toResponse(DeploymentProcessHasSoftwareVersionInfo entity) {
        var version = entity.getSoftwareVersion();
        var software = version.getSoftware();
        var versionResponse = new SoftwareNameAndVersionResponse(
                version.getId(),
                version.getName(),
                software.getId(),
                software.getName()
        );
        return new DeploymentProcessHasSoftwareVersionResponse(
                entity.getId(),
                entity.getCustomer().getId(),
                versionResponse
        );
    }

}
