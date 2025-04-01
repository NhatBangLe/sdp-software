package io.github.nhatbangle.sdp.software.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app")
public class AppConfigProps {

    private String mailBoxQueue;
    private String notificationBoxQueue;

}
