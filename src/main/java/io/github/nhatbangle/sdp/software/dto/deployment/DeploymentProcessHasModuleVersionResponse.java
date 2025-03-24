package io.github.nhatbangle.sdp.software.dto.deployment;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasModuleVersion}
 */
public record DeploymentProcessHasModuleVersionResponse(
        Long processId,
        ModuleVersion version,
        Module module
) implements Serializable {
    /**
     * DTO for {@link io.github.nhatbangle.sdp.software.entity.module.ModuleVersion}
     */
    public record ModuleVersion(String id, String name) implements Serializable {
    }
    /**
     * DTO for {@link io.github.nhatbangle.sdp.software.entity.module.Module}
     */
    public record Module(String id, String name) implements Serializable {
    }
}