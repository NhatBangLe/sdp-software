package io.github.nhatbangle.sdp.software.controller.software;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareCreateRequest;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareResponse;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareUpdateRequest;
import io.github.nhatbangle.sdp.software.service.software.SoftwareService;
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
@Tag(name = "Software")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software")
public class SoftwareController {

    private final SoftwareService service;

    @GetMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<SoftwareResponse> getAllByUserId(
            @PathVariable @UUID String userId,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByUserId(
                userId,
                name,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{softwareId}")
    @ResponseStatus(HttpStatus.OK)
    public SoftwareResponse getSoftwareById(
            @PathVariable @UUID String softwareId
    ) {
        return service.getById(softwareId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SoftwareResponse createSoftware(
            @PathVariable @UUID String userId,
            @RequestBody @Valid SoftwareCreateRequest request
    ) {
        return service.create(userId, request);
    }

    @PutMapping("/{softwareId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSoftwareById(
            @PathVariable @UUID String softwareId,
            @RequestBody @Valid SoftwareUpdateRequest request
    ) {
        service.updateById(softwareId, request);
    }

    @DeleteMapping("/{softwareId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSoftwareById(
            @PathVariable @UUID String softwareId
    ) {
        service.deleteById(softwareId);
    }

}
