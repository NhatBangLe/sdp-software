package io.github.nhatbangle.sdp.software.constant;

import lombok.Getter;

@Getter
public enum MailTemplatePlaceholder {
    CUSTOMER_NAME("${customer}"),
    DEPLOYMENT_PROCESS_ID("${process_id}"),
    SOFTWARE_NAME("${software}"),
    SOFTWARE_VERSION("${software_version}"),
    LICENSE_ID("${license_id}"),
    LICENSE_START_TIME("${license_start_time}"),
    LICENSE_END_TIME("${license_end_time}"),
    ;

    final String varName;
    MailTemplatePlaceholder(String varName) {
        this.varName = varName;
    }
}
