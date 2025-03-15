package io.github.nhatbangle.sdp.software.mapper.deployment;

import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentPhaseResponse;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentPhaseInfo;
import io.github.nhatbangle.sdp.software.projection.deployment.DeploymentPhaseTypeName;

public class DeploymentPhaseMapper {

    public DeploymentPhaseResponse toResponse(DeploymentPhaseInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        var userLastUpdated = entity.getUserLastUpdate();
        var actualStartDate = entity.getActualStartDate();
        var actualEndDate = entity.getActualEndDate();
        return new DeploymentPhaseResponse(
                entity.getId(),
                entity.getNumOrder(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                phaseTypeNameToResponse(entity.getType()),
                entity.getPlannedStartDate().toString(),
                entity.getPlannedEndDate().toString(),
                actualStartDate != null ? actualStartDate.toString() : null,
                actualEndDate != null ? actualEndDate.toString() : null,
                entity.getIsDone(),
                userLastUpdated != null ? userLastUpdated.getId() : null
        );
    }

    public DeploymentPhaseResponse toResponse(DeploymentPhase entity) {
        var actualEndDate = entity.getActualEndDate();
        var actualStartDate = entity.getActualStartDate();
        var updatedAt = entity.getUpdatedAt();
        var userLastUpdated = entity.getUserLastUpdate();
        return new DeploymentPhaseResponse(
                entity.getId(),
                entity.getNumOrder(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                phaseTypeNameToResponse(entity.getType()),
                entity.getPlannedStartDate().toString(),
                entity.getPlannedEndDate().toString(),
                actualStartDate != null ? actualStartDate.toString() : null,
                actualEndDate != null ? actualEndDate.toString() : null,
                entity.getIsDone(),
                userLastUpdated != null ? userLastUpdated.getId() : null
        );
    }

    private DeploymentPhaseResponse.DeploymentPhaseType phaseTypeNameToResponse(DeploymentPhaseType phaseTypeName) {
        return new DeploymentPhaseResponse.DeploymentPhaseType(phaseTypeName.getName());
    }

    private DeploymentPhaseResponse.DeploymentPhaseType phaseTypeNameToResponse(DeploymentPhaseTypeName phaseTypeName) {
        return new DeploymentPhaseResponse.DeploymentPhaseType(phaseTypeName.getName());
    }

}
