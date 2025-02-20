package io.github.nhatbangle.sdp.software.projection.customer;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.Customer}
 */
public interface CustomerInfo {
    String getId();

    String getName();

    String getEmail();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}