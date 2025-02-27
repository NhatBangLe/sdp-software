package io.github.nhatbangle.sdp.software.dto.module;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.module.ModuleVersion}
 */
public record ModuleNameAndVersionResponse(
        String versionId,
        String versionName,
        String moduleId,
        String moduleName
) implements Serializable {
}