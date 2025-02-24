package io.github.nhatbangle.sdp.software.mapper.deployment;

import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseResponse;
import io.github.nhatbangle.sdp.software.entity.SoftwareLicense;
import io.github.nhatbangle.sdp.software.projection.SoftwareLicenseInfo;

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

}
