package io.github.nhatbangle.sdp.software.mapper.software;

import io.github.nhatbangle.sdp.software.dto.software.SoftwareNameAndVersionResponse;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareVersionResponse;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareVersionIdName;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareVersionInfo;

public class SoftwareVersionMapper {

    public SoftwareVersionResponse toResponse(SoftwareVersionInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new SoftwareVersionResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public SoftwareVersionResponse toResponse(SoftwareVersion entity) {
        var updatedAt = entity.getUpdatedAt();
        return new SoftwareVersionResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public SoftwareNameAndVersionResponse toNameResponse(SoftwareVersionIdName entity) {
        var software = entity.getSoftware();
        return new SoftwareNameAndVersionResponse(
                entity.getId(),
                entity.getName(),
                software.getId(),
                software.getName()
        );
    }

}
