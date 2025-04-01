package io.github.nhatbangle.sdp.software.dto.notification;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record NotificationSendPayload(
        @NotNull String title,
        @Nullable String description,
        @NotNull List<@UUID @NotNull String> recipientIds
) {
}
