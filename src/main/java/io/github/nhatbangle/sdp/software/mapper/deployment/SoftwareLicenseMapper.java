package io.github.nhatbangle.sdp.software.mapper.deployment;

import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseDetailResponse;
import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseResponse;
import io.github.nhatbangle.sdp.software.entity.SoftwareLicense;
import io.github.nhatbangle.sdp.software.projection.SoftwareLicenseInfo;
import io.github.nhatbangle.sdp.software.projection.deployment.SoftwareLicenseDetailInfo;

public class SoftwareLicenseMapper {

    public SoftwareLicenseResponse toResponse(SoftwareLicenseInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new SoftwareLicenseResponse(
                entity.getId(),
                entity.getDescription(),
                entity.getStartTime().toEpochMilli(),
                entity.getEndTime().toEpochMilli(),
                entity.getExpireAlertIntervalDay(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public SoftwareLicenseResponse toResponse(SoftwareLicense entity) {
        var updatedAt = entity.getUpdatedAt();
        return new SoftwareLicenseResponse(
                entity.getId(),
                entity.getDescription(),
                entity.getStartTime().toEpochMilli(),
                entity.getEndTime().toEpochMilli(),
                entity.getExpireAlertIntervalDay(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public SoftwareLicenseDetailResponse toResponse(SoftwareLicenseDetailInfo entity) {
        var processEntity = entity.getProcess();
        var customer = processEntity.getCustomer();
        var softwareVersion = processEntity.getSoftwareVersion();
        var updatedAt = entity.getUpdatedAt();
        var process = new SoftwareLicenseDetailResponse.DeploymentProcessDto(
                processEntity.getId(),
                softwareVersion.getId(),
                softwareVersion.getName(),
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                processEntity.getCreator().getId()
        );
        return new SoftwareLicenseDetailResponse(
                entity.getId(),
                entity.getDescription(),
                entity.getStartTime().toEpochMilli(),
                entity.getEndTime().toEpochMilli(),
                entity.getExpireAlertIntervalDay(),
                entity.getIsExpireAlertDone(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                process,
                entity.getCreator().getId()
        );
    }

}
