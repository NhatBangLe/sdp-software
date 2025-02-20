package io.github.nhatbangle.sdp.software.controller.deployment;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.deployment.*;
import io.github.nhatbangle.sdp.software.service.deployment.DeploymentPhaseTypeService;
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
@Tag(name = "Deployment Phase Type")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/deployment-process/phase/type")
public class DeploymentPhaseTypeController {

    private final DeploymentPhaseTypeService service;

    @GetMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DeploymentPhaseTypeResponse> getAllByProcessId(
            @PathVariable @UUID String userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAll(userId, name, pageNumber, pageSize);
    }

    @GetMapping("/{typeId}")
    @ResponseStatus(HttpStatus.OK)
    public DeploymentPhaseTypeResponse getById(
            @PathVariable @UUID String typeId
    ) {
        return service.getById(typeId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DeploymentPhaseTypeResponse create(
            @PathVariable @UUID String userId,
            @RequestBody @Valid DeploymentPhaseTypeCreateRequest request
    ) {
        return service.create(userId, request);
    }

    @PutMapping("/{typeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String typeId,
            @RequestBody @Valid DeploymentPhaseTypeUpdateRequest request
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
