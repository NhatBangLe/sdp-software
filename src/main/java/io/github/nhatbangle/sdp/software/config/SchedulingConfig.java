package io.github.nhatbangle.sdp.software.config;

import io.github.nhatbangle.sdp.software.service.deployment.SoftwareLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {

    private final SoftwareLicenseService licenseService;

    @Scheduled(cron = "0 0 0 * * *")
    public void alertAlmostExpiredLicenses() {
        licenseService.sendExpirationAlertMail();
    }

}
