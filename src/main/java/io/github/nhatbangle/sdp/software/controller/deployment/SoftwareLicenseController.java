package io.github.nhatbangle.sdp.software.controller.deployment;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseCreateRequest;
import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseResponse;
import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseUpdateRequest;
import io.github.nhatbangle.sdp.software.service.deployment.SoftwareLicenseService;
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
@RequiredArgsConstructor
@Tag(name = "Software License")
@RequestMapping("/api/v1/software/deployment-process/license")
public class SoftwareLicenseController {

    private final SoftwareLicenseService service;

    @GetMapping("/{processId}/process")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<SoftwareLicenseResponse> getAllByProcessId(
            @PathVariable @Min(0) Long processId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByProcessId(processId, pageNumber, pageSize);
    }

    @GetMapping("/{licenseId}")
    @ResponseStatus(HttpStatus.OK)
    public SoftwareLicenseResponse getById(
            @PathVariable @UUID String licenseId
    ) {
        return service.getById(licenseId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SoftwareLicenseResponse create(
            @PathVariable @UUID String userId,
            @RequestBody @Valid SoftwareLicenseCreateRequest request
    ) {
        return service.create(userId, request);
    }

    @PutMapping("/{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String licenseId,
            @RequestBody @Valid SoftwareLicenseUpdateRequest request
    ) {
        service.updateById(licenseId, request);
    }

    @DeleteMapping("/{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String licenseId
    ) {
        service.deleteById(licenseId);
    }

}
