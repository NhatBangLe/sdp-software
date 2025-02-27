package io.github.nhatbangle.sdp.software.mapper.deployment;

import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessResponse;
import io.github.nhatbangle.sdp.software.entity.Customer;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion;
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

}
