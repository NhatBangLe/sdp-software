package io.github.nhatbangle.sdp.software.controller.module;

import io.github.nhatbangle.sdp.software.dto.AttachmentUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.document.*;
import io.github.nhatbangle.sdp.software.service.module.ModuleDocumentService;
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
@Tag(name = "Module Document")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/module/document")
public class ModuleDocumentController {

    private final ModuleDocumentService service;

    @GetMapping("/{moduleVersionId}/version")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleDocumentResponse> getAllByVersionId(
            @PathVariable @UUID String moduleVersionId,
            @RequestParam(required = false) String documentTypeName,
            @RequestParam(required = false) String documentName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByVersionId(
                moduleVersionId,
                documentTypeName,
                documentName,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDocumentResponse getById(
            @PathVariable @UUID String documentId
    ) {
        return service.getById(documentId);
    }

    @PostMapping("/{moduleVersionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleDocumentResponse create(
            @PathVariable @UUID String moduleVersionId,
            @RequestBody @Valid ModuleDocumentCreateRequest request
    ) {
        return service.create(moduleVersionId, request);
    }

    @PutMapping("/{documentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String documentId,
            @RequestBody @Valid ModuleDocumentUpdateRequest request
    ) {
        service.updateById(documentId, request);
    }

    @PutMapping("/{documentId}/attachment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAttachment(
            @PathVariable @UUID String documentId,
            @RequestBody @Valid AttachmentUpdateRequest request
    ) {
        service.updateAttachment(documentId, request);
    }

    @DeleteMapping("/{documentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String documentId
    ) {
        service.deleteById(documentId);
    }

}
