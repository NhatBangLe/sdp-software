package io.github.nhatbangle.sdp.software.mapper.deployment;

import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentPhaseTypeResponse;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentPhaseTypeInfo;

public class DeploymentPhaseTypeMapper {

    public DeploymentPhaseTypeResponse toResponse(DeploymentPhaseTypeInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new DeploymentPhaseTypeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public DeploymentPhaseTypeResponse toResponse(DeploymentPhaseType entity) {
        var updatedAt = entity.getUpdatedAt();
        return new DeploymentPhaseTypeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

}
