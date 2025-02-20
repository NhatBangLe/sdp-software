package io.github.nhatbangle.sdp.software.mapper.deployment;

import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessResponse;
import io.github.nhatbangle.sdp.software.entity.Customer;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion;
import io.github.nhatbangle.sdp.software.projection.customer.CustomerName;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentProcessInfo;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareVersionName;

public class DeploymentProcessMapper {

    public DeploymentProcessResponse toResponse(DeploymentProcessInfo entity) {
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

    private DeploymentProcessResponse.Software softwareVersionToResponse(SoftwareVersionName versionInfo) {
        return new DeploymentProcessResponse.Software(
                versionInfo.getName(),
                versionInfo.getSoftware().getName()
        );
    }

    private DeploymentProcessResponse.Software softwareVersionToResponse(SoftwareVersion version) {
        return new DeploymentProcessResponse.Software(
                version.getName(),
                version.getSoftware().getName()
        );
    }

    private DeploymentProcessResponse.Customer customerToResponse(CustomerName customer) {
        return new DeploymentProcessResponse.Customer(
                customer.getName()
        );
    }

    private DeploymentProcessResponse.Customer customerToResponse(Customer customer) {
        return new DeploymentProcessResponse.Customer(
                customer.getName()
        );
    }

}
