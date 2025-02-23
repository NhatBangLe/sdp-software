package io.github.nhatbangle.sdp.software.constant;

public enum MailTemplatePlaceholder {
    CUSTOMER_NAME("${customer}"),
    DEPLOYMENT_PROCESS_ID("${process_id}"),
    SOFTWARE_NAME("${software}"),
    SOFTWARE_VERSION("${software_version}"),
    ;

    final String name;
    MailTemplatePlaceholder(String name) {
        this.name = name;
    }
}
