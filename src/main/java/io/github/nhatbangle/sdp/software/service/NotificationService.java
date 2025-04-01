package io.github.nhatbangle.sdp.software.service;

import io.github.nhatbangle.sdp.software.dto.notification.MailSendPayload;
import io.github.nhatbangle.sdp.software.dto.notification.NotificationSendPayload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class NotificationService {

    @Value("${app.mail-box-queue}")
    private String mailBoxQueue;
    @Value("${app.notification-box-queue}")
    private String notificationBoxQueue;
    private final RabbitTemplate rabbitTemplate;

    public boolean sendMail(@NotNull @Valid MailSendPayload payload) {
        try {
            rabbitTemplate.convertAndSend(mailBoxQueue, payload);
            return true;
        } catch (AmqpException e) {
            amqpExceptionHandler(e);
            return false;
        }
    }

    public boolean sendNotification(@NotNull @Valid NotificationSendPayload payload) {
        try {
            rabbitTemplate.convertAndSend(notificationBoxQueue, payload);
            return true;
        } catch (AmqpException e) {
            amqpExceptionHandler(e);
            return false;
        }
    }

    private void amqpExceptionHandler(AmqpException e) {
        log.warn("""
                        Could not send expiration alert mail.
                        Because the AMQP exception has been thrown.
                        """);
        log.error(e.getMessage(), e);
    }

}
