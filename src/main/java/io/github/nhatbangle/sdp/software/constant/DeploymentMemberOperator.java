package io.github.nhatbangle.sdp.software.constant;

import lombok.Getter;

@Getter
public enum DeploymentMemberOperator {
    ADD("ADD"),
    REMOVE("REMOVE");

    private final String name;

    DeploymentMemberOperator(String name) {
        this.name = name;
    }
}
