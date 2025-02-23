package io.github.nhatbangle.sdp.software.constant;

public enum MailTemplateType {
    SOFTWARE_EXPIRE_ALERT("SOFTWARE_EXPIRE_ALERT"),
    SOFTWARE_DEPLOYED_SUCCESSFULLY("SOFTWARE_DEPLOYED_SUCCESSFULLY");

    final String name;

    MailTemplateType(String name) {
        this.name = name;
    }
}
