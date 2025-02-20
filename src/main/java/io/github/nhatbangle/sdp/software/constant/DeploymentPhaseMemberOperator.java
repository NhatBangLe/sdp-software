package io.github.nhatbangle.sdp.software.constant;

import lombok.Getter;

@Getter
public enum DeploymentPhaseMemberOperator {
    ADD("ADD"),
    REMOVE("REMOVE");

    private final String name;

    DeploymentPhaseMemberOperator(String name) {
        this.name = name;
    }
}
