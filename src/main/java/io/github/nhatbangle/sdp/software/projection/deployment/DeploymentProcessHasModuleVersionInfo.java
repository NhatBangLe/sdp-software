package io.github.nhatbangle.sdp.software.projection.deployment;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasModuleVersion}
 */
public interface DeploymentProcessHasModuleVersionInfo {
    DeploymentProcessInfo getProcess();

    ModuleVersionInfo getVersion();

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
     */
    interface DeploymentProcessInfo {
        Long getId();
    }

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.module.ModuleVersion}
     */
    interface ModuleVersionInfo {
        String getId();

        String getName();

        ModuleInfo getModule();

        /**
         * Projection for {@link io.github.nhatbangle.sdp.software.entity.module.Module}
         */
        interface ModuleInfo {
            String getId();

            String getName();
        }
    }
}