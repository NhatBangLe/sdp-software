package io.github.nhatbangle.sdp.software.controller;

import io.github.nhatbangle.sdp.software.dto.document.DocumentTypeCreateRequest;
import io.github.nhatbangle.sdp.software.dto.document.DocumentTypeResponse;
import io.github.nhatbangle.sdp.software.dto.document.DocumentTypeUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.service.DocumentTypeService;
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
@Tag(name = "Document Type")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/document-type")
public class DocumentTypeController {

    private final DocumentTypeService service;

    @GetMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DocumentTypeResponse> getAll(
            @PathVariable @UUID String userId,
            @RequestParam(required = false) String documentTypeName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAll(
                userId,
                documentTypeName,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{typeId}")
    @ResponseStatus(HttpStatus.OK)
    public DocumentTypeResponse getById(
            @PathVariable @UUID String typeId
    ) {
        return service.getById(typeId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentTypeResponse create(
            @PathVariable @UUID String userId,
            @RequestBody @Valid DocumentTypeCreateRequest request
    ) {
        return service.create(userId, request);
    }

    @PutMapping("/{typeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String typeId,
            @RequestBody @Valid DocumentTypeUpdateRequest request
    ) {
        service.updateById(typeId, request);
    }

    @DeleteMapping("/{typeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String typeId
    ) {
        service.deleteById(typeId);
    }

}
