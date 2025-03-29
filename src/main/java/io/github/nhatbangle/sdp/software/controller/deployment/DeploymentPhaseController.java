package io.github.nhatbangle.sdp.software.controller.deployment;

import io.github.nhatbangle.sdp.software.dto.AttachmentUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.deployment.*;
import io.github.nhatbangle.sdp.software.service.deployment.DeploymentPhaseService;
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
@Tag(name = "Deployment Phase")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/deployment-process/phase")
public class DeploymentPhaseController {

    private final DeploymentPhaseService service;

    @GetMapping("/{processId}/process")
    @ResponseStatus(HttpStatus.OK)
    public List<DeploymentPhaseResponse> getAllByProcessId(
            @PathVariable @Min(0) Long processId
    ) {
        return service.getAllByProcessId(processId);
    }

    @GetMapping("/{processId}/histories")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DeploymentPhaseHistoryResponse> getHistories(
            @PathVariable @Min(0) Long processId,
            @RequestParam(required = false) String phaseTypeName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getHistoriesByProcessId(processId, phaseTypeName, description, pageNumber, pageSize);
    }

    @GetMapping("/{phaseId}")
    @ResponseStatus(HttpStatus.OK)
    public DeploymentPhaseResponse getById(
            @PathVariable @UUID String phaseId
    ) {
        return service.getById(phaseId);
    }

    @GetMapping("/{phaseId}/attachment")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllAttachments(
            @PathVariable @UUID String phaseId
    ) {
        return service.getAllAttachments(phaseId);
    }

    @GetMapping("/{phaseId}/member")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllMembers(
            @PathVariable @UUID String phaseId
    ) {
        return service.getAllMembers(phaseId);
    }

    @PostMapping("/{processId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DeploymentPhaseResponse create(
            @PathVariable @Min(0) Long processId,
            @RequestBody @Valid DeploymentPhaseCreateRequest request
    ) {
        return service.create(processId, request);
    }

    @PutMapping("/{phaseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String phaseId,
            @RequestBody @Valid DeploymentPhaseUpdateRequest request
    ) {
        service.updateById(phaseId, request);
    }

    @PutMapping("/{phaseId}/attachment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAttachment(
            @PathVariable @UUID String phaseId,
            @RequestBody @Valid AttachmentUpdateRequest request
    ) {
        service.updateAttachment(phaseId, request);
    }

    @PutMapping("/{phaseId}/member")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(
            @PathVariable @UUID String phaseId,
            @RequestBody @Valid DeploymentPhaseMemberUpdateRequest request
    ) {
        service.updateMember(phaseId, request);
    }

    @PutMapping("/{phaseId}/actual")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateActualDatesById(
            @PathVariable @UUID String phaseId,
            @RequestBody @Valid DeploymentPhaseUpdateActualDatesRequest request
    ) {
        service.updateActualDates(phaseId, request);
    }

    @DeleteMapping("/{phaseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String phaseId
    ) {
        service.deleteById(phaseId);
    }

}
