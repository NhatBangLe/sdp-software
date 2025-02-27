package io.github.nhatbangle.sdp.software.projection.module;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.module.ModuleVersion}
 */
public interface ModuleVersionIdName {
    String getId();

    String getName();

    ModuleIdName getModule();
}