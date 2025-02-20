package io.github.nhatbangle.sdp.software.constant;

import lombok.Getter;

@Getter
public enum DeploymentProcessMemberOperator {
    ADD("ADD"),
    REMOVE("REMOVE");

    private final String name;

    DeploymentProcessMemberOperator(String name) {
        this.name = name;
    }
}
