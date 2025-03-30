package io.github.nhatbangle.sdp.software.projection.deployment;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.SoftwareLicense}
 */
public interface SoftwareLicenseDetailInfo {
    String getId();

    @Nullable
    String getDescription();

    Instant getStartTime();

    Instant getEndTime();

    Integer getExpireAlertIntervalDay();

    @Value("#{target.isExpireAlertDone}")
    boolean getIsExpireAlertDone();

    Instant getCreatedAt();

    @Nullable
    Instant getUpdatedAt();

    DeploymentProcessInfo getProcess();

    UserInfo getCreator();

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
     */
    interface DeploymentProcessInfo {
        Long getId();

        SoftwareVersionInfo getSoftwareVersion();

        CustomerInfo getCustomer();

        UserInfo getCreator();

        /**
         * Projection for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion}
         */
        interface SoftwareVersionInfo {
            String getId();

            String getName();

            SoftwareInfo getSoftware();

            /**
             * Projection for {@link io.github.nhatbangle.sdp.software.entity.software.Software}
             */
            interface SoftwareInfo {
                String getId();

                String getName();
            }
        }

        /**
         * Projection for {@link io.github.nhatbangle.sdp.software.entity.Customer}
         */
        interface CustomerInfo {
            String getId();

            String getName();

            String getEmail();
        }

    }

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.User}
     */
    interface UserInfo {
        String getId();
    }
}