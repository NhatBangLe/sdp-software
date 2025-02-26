package io.github.nhatbangle.sdp.software.dto.software;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion}
 */
public record SoftwareNameAndVersionResponse(
        String versionId,
        String versionName,
        String softwareId,
        String softwareName
) implements Serializable {
}