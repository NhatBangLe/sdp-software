package io.github.nhatbangle.sdp.software.mapper.software;

import io.github.nhatbangle.sdp.software.dto.software.SoftwareResponse;
import io.github.nhatbangle.sdp.software.entity.software.Software;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareInfo;

public class SoftwareMapper {

    public SoftwareResponse toResponse(SoftwareInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new SoftwareResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public SoftwareResponse toResponse(Software entity) {
        var updatedAt = entity.getUpdatedAt();
        return new SoftwareResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

}
