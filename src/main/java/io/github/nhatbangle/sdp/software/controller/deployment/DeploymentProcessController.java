package io.github.nhatbangle.sdp.software.controller.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.deployment.*;
import io.github.nhatbangle.sdp.software.service.deployment.DeploymentProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@Tag(name = "Deployment Process")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/deployment-process")
public class DeploymentProcessController {

    private final DeploymentProcessService service;

    @GetMapping("/{userId}/creator")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DeploymentProcessResponse> getAllByUserId(
            @PathVariable @UUID String userId,
            @RequestParam(required = false) String softwareName,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) DeploymentProcessStatus status,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByCreatorId(
                userId,
                softwareName,
                customerName,
                status,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{memberId}/join")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DeploymentProcessResponse> getAllByMemberId(
            @PathVariable @UUID String memberId,
            @RequestParam(required = false) String softwareName,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) DeploymentProcessStatus status,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByMemberId(
                memberId,
                softwareName,
                customerName,
                status,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{customerId}/customer")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DeploymentProcessHasSoftwareVersionResponse> getAllByCustomerId(
            @PathVariable @UUID String customerId,
            @RequestParam(required = false) String softwareName,
            @RequestParam(required = false) String softwareVersionName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByCustomerId(
                customerId,
                softwareName,
                softwareVersionName,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{processId}")
    @ResponseStatus(HttpStatus.OK)
    public DeploymentProcessResponse getById(
            @PathVariable @Min(0) Long processId
    ) {
        return service.getById(processId);
    }

    @GetMapping("/{processId}/module-version")
    @ResponseStatus(HttpStatus.OK)
    public List<DeploymentProcessHasModuleVersionResponse> getAllModuleVersions(
            @PathVariable @Min(0) Long processId
    ) {
        return service.getAllModuleVersions(processId);
    }

    @GetMapping("/{processId}/member")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllMembers(
            @PathVariable @Min(0) Long processId
    ) {
        return service.getAllMembers(processId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DeploymentProcessResponse create(
            @PathVariable @UUID String userId,
            @RequestBody @Valid DeploymentProcessCreateRequest request
    ) {
        return service.create(userId, request);
    }

    @PutMapping("/{processId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @Min(0) Long processId,
            @RequestBody @Valid DeploymentProcessUpdateRequest request
    ) {
        service.updateById(processId, request);
    }

    @PutMapping("/{processId}/member")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(
            @PathVariable @Min(0) Long processId,
            @RequestBody @Valid DeploymentProcessMemberUpdateRequest request
    ) {
        service.updateMember(processId, request);
    }

    @DeleteMapping("/{processId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @Min(0) Long processId
    ) {
        service.deleteById(processId);
    }

}
