package io.github.nhatbangle.sdp.software.constant;

import lombok.Getter;

@Getter
public enum DeploymentProcessStatus {
    INIT("INIT"),
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String name;
    DeploymentProcessStatus(String name) {
        this.name = name;
    }
}
