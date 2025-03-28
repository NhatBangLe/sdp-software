package io.github.nhatbangle.sdp.software.controller;

import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import io.github.nhatbangle.sdp.software.dto.mail.MailTemplateCreateRequest;
import io.github.nhatbangle.sdp.software.dto.mail.MailTemplateResponse;
import io.github.nhatbangle.sdp.software.dto.mail.MailTemplateUpdateRequest;
import io.github.nhatbangle.sdp.software.service.MailTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Validated
@RestController
@Tag(name = "Mail Template")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/mail-template")
public class MailTemplateController {

    private final MailTemplateService service;

    @GetMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.OK)
    public MailTemplateResponse getByUserId(
            @PathVariable @UUID String userId,
            @RequestParam MailTemplateType type
    ) {
        return service.getByUserIdAndType(userId, type);
    }

    @GetMapping("/{templateId}")
    @ResponseStatus(HttpStatus.OK)
    public MailTemplateResponse getById(
            @PathVariable @UUID String templateId
    ) {
        return service.getById(templateId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MailTemplateResponse create(
            @PathVariable @UUID String userId,
            @RequestBody @Valid MailTemplateCreateRequest request
    ) {
        return service.create(userId, request);
    }

    @PutMapping("/{templateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String templateId,
            @RequestBody @Valid MailTemplateUpdateRequest request
    ) {
        service.updateById(templateId, request);
    }

    @DeleteMapping("/{templateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String templateId
    ) {
        service.deleteById(templateId);
    }

}
