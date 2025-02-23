package io.github.nhatbangle.sdp.software.controller.software;

import io.github.nhatbangle.sdp.software.dto.*;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentCreateRequest;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentResponse;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentUpdateRequest;
import io.github.nhatbangle.sdp.software.service.software.SoftwareDocumentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@Tag(name = "Software Document")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/document")
public class SoftwareDocumentController {

    private final SoftwareDocumentService service;

    @GetMapping("/{softwareVersionId}/version")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<SoftwareDocumentResponse> getAllByVersionId(
            @PathVariable @UUID String softwareVersionId,
            @RequestParam(required = false) String documentTypeName,
            @RequestParam(required = false) String documentName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByVersionId(
                softwareVersionId,
                documentTypeName,
                documentName,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    public SoftwareDocumentResponse getById(
            @PathVariable @UUID String documentId
    ) {
        return service.getById(documentId);
    }

    @PostMapping("/{softwareVersionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SoftwareDocumentResponse create(
            @PathVariable @UUID String softwareVersionId,
            @RequestBody @Valid SoftwareDocumentCreateRequest request
    ) {
        return service.create(softwareVersionId, request);
    }

    @PutMapping("/{documentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String documentId,
            @RequestBody @Valid SoftwareDocumentUpdateRequest request
    ) {
        service.updateById(documentId, request);
    }

    @PutMapping("/{documentId}/attachment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAttachment(
            @PathVariable @UUID String documentId,
            @RequestBody @Valid AttachmentUpdateRequest request
    ) {
        service.updateAttachmentById(documentId, request);
    }

    @DeleteMapping("/{documentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String documentId
    ) {
        service.deleteById(documentId);
    }

}
