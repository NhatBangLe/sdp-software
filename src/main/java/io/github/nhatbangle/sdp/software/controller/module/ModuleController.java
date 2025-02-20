package io.github.nhatbangle.sdp.software.controller.module;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.module.ModuleCreateRequest;
import io.github.nhatbangle.sdp.software.dto.module.ModuleResponse;
import io.github.nhatbangle.sdp.software.dto.module.ModuleUpdateRequest;
import io.github.nhatbangle.sdp.software.service.module.ModuleService;
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
@Tag(name = "Module")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/module")
public class ModuleController {

    private final ModuleService service;

    @GetMapping("/{softwareVersionId}/software-version")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleResponse> getAllByVersionId(
            @PathVariable @UUID String softwareVersionId,
            @RequestParam(required = false, defaultValue = "") String moduleName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAllByVersionId(
                softwareVersionId,
                moduleName,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleResponse getById(
            @PathVariable @UUID String moduleId
    ) {
        return service.getById(moduleId);
    }

    @PostMapping("/{softwareVersionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleResponse create(
            @PathVariable @UUID String softwareVersionId,
            @RequestBody @Valid ModuleCreateRequest request
    ) {
        return service.create(softwareVersionId, request);
    }

    @PutMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String moduleId,
            @RequestBody @Valid ModuleUpdateRequest request
    ) {
        service.updateById(moduleId, request);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String moduleId
    ) {
        service.deleteById(moduleId);
    }

}
