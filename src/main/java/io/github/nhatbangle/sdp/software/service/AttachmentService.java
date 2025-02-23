package io.github.nhatbangle.sdp.software.service;

import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;

@Service
@Validated
@RequiredArgsConstructor
public class AttachmentService {

    private final MessageSource messageSource;

    public boolean isFileExist(@UUID @NotNull String fileId) throws ServiceUnavailableException {
        return true;
    }

    public void foundOrElseThrow(@NotNull @UUID String fileId, boolean validateResult)
            throws IllegalArgumentException {
        if (!validateResult) {
            var message = messageSource.getMessage(
                    "attachment.not_found",
                    new Object[]{fileId},
                    Locale.getDefault()
            );
            throw new IllegalArgumentException(message);
        }
    }

}
