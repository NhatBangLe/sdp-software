package io.github.nhatbangle.sdp.software.controller.software;

import io.github.nhatbangle.sdp.software.dto.*;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareVersionCreateRequest;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareVersionResponse;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareVersionUpdateRequest;
import io.github.nhatbangle.sdp.software.service.software.SoftwareVersionService;
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
@Tag(name = "Software Version")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/version")
public class SoftwareVersionController {

    private final SoftwareVersionService service;

    @GetMapping("/{softwareId}/software")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<SoftwareVersionResponse> getAllVersionsBySoftwareId(
            @PathVariable @UUID String softwareId,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllBySoftwareId(
                softwareId,
                name,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{versionId}")
    @ResponseStatus(HttpStatus.OK)
    public SoftwareVersionResponse getVersionById(
            @PathVariable @UUID String versionId
    ) {
        return service.getById(versionId);
    }

    @PostMapping("/{softwareId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SoftwareVersionResponse createVersion(
            @PathVariable @UUID String softwareId,
            @RequestBody @Valid SoftwareVersionCreateRequest request
    ) {
        return service.create(softwareId, request);
    }

    @PutMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVersionById(
            @PathVariable @UUID String versionId,
            @RequestBody @Valid SoftwareVersionUpdateRequest request
    ) {
        service.updateById(versionId, request);
    }

    @DeleteMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVersionById(
            @PathVariable @UUID String versionId
    ) {
        service.deleteById(versionId);
    }

}
