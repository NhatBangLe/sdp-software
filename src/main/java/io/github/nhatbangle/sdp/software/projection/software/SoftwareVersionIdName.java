package io.github.nhatbangle.sdp.software.projection.software;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion}
 */
public interface SoftwareVersionIdName {
    String getId();

    String getName();

    SoftwareIdName getSoftware();
}