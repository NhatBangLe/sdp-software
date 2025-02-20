package io.github.nhatbangle.sdp.software.controller.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessCreateRequest;
import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessMemberUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessResponse;
import io.github.nhatbangle.sdp.software.dto.deployment.DeploymentProcessUpdateRequest;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DeploymentProcessResponse> getAllByVersionIdOrCustomerId(
            @RequestParam(required = false) String softwareVersionName,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) DeploymentProcessStatus status,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAll(
                softwareVersionName,
                customerName,
                status,
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

    @GetMapping("/{processId}/members")
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
