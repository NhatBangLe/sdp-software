package io.github.nhatbangle.sdp.software.controller.module;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.module.*;
import io.github.nhatbangle.sdp.software.service.module.ModuleVersionService;
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
@Tag(name = "Module Version")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/module/version")
public class ModuleVersionController {

    private final ModuleVersionService service;

    @GetMapping("/{softwareVersionId}/software-version")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleNameAndVersionResponse> getAllBySoftwareVersionId(
            @PathVariable @UUID String softwareVersionId,
            @RequestParam(required = false) String moduleName,
            @RequestParam(required = false) String versionName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllBySoftwareVersionId(
                softwareVersionId,
                moduleName,
                versionName,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{moduleId}/module")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleVersionResponse> getAllByModuleId(
            @PathVariable @UUID String moduleId,
            @RequestParam(required = false, defaultValue = "") String moduleName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByModuleId(
                moduleId,
                moduleName,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{versionId}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleVersionResponse getById(
            @PathVariable @UUID String versionId
    ) {
        return service.getById(versionId);
    }

    @PostMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleVersionResponse create(
            @PathVariable @UUID String moduleId,
            @RequestBody @Valid ModuleVersionCreateRequest request
    ) {
        return service.create(moduleId, request);
    }

    @PutMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String versionId,
            @RequestBody @Valid ModuleVersionUpdateRequest request
    ) {
        service.updateById(versionId, request);
    }

    @DeleteMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String versionId
    ) {
        service.deleteById(versionId);
    }

}
