package io.github.nhatbangle.sdp.software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SdpSoftwareApplication {

    public static void main(String[] args) {
        SpringApplication.from(SdpSoftwareApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
