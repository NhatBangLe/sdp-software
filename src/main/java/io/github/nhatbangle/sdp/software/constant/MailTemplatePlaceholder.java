package io.github.nhatbangle.sdp.software.constant;

public enum MailTemplatePlaceholder {
    CUSTOMER_NAME("${customer}"),
    DEPLOYMENT_PROCESS_ID("${process_id}"),
    SOFTWARE_NAME("${software}"),
    SOFTWARE_VERSION("${software_version}"),
    LICENSE_ID("${license_id}"),
    LICENSE_START_TIME("${license_start_time}"),
    LICENSE_END_TIME("${license_end_time}"),
    ;

    final String name;
    MailTemplatePlaceholder(String name) {
        this.name = name;
    }
}
