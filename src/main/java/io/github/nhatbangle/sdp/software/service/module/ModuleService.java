package io.github.nhatbangle.sdp.software.service.module;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.module.ModuleCreateRequest;
import io.github.nhatbangle.sdp.software.dto.module.ModuleResponse;
import io.github.nhatbangle.sdp.software.dto.module.ModuleUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.module.Module;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleMapper;
import io.github.nhatbangle.sdp.software.repository.module.ModuleRepository;
import io.github.nhatbangle.sdp.software.service.software.SoftwareVersionService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Service
@Validated
@CacheConfig(cacheNames = "sdp_software-module")
@RequiredArgsConstructor
public class ModuleService {

    private final MessageSource messageSource;
    private final SoftwareVersionService softwareVersionService;
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @NotNull
    public PagingWrapper<ModuleResponse> getAllByVersionId(
            @UUID @NotNull String softwareVersionId,
            @Nullable String name,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = moduleRepository
                .findAllBySoftwareVersion_IdAndNameContainsIgnoreCase(
                        softwareVersionId,
                        Objects.requireNonNullElse(name, ""),
                        pageable
                )
                .map(moduleMapper::toResponse);
        return PagingWrapper.fromPage(page);
    }

    @NotNull
    @Cacheable(key = "#moduleId")
    public ModuleResponse getById(
            @UUID @NotNull String moduleId
    ) throws NoSuchElementException {
        var module = moduleRepository.findInfoById(moduleId)
                .orElseThrow(() -> notFoundHandler(moduleId));
        return moduleMapper.toResponse(module);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public ModuleResponse create(
            @UUID @NotNull String softwareVersionId,
            @NotNull @Valid ModuleCreateRequest request
    ) throws IllegalArgumentException {
        var softwareVersion = softwareVersionService.findById(softwareVersionId);
        var module = moduleRepository.save(Module.builder()
                .name(request.name())
                .description(request.description())
                .softwareVersion(softwareVersion)
                .build());
        return moduleMapper.toResponse(module);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#moduleId")
    public ModuleResponse updateById(
            @UUID @NotNull String moduleId,
            @NotNull @Valid ModuleUpdateRequest request
    ) throws NoSuchElementException {
        var module = findById(moduleId);
        module.setName(request.name());
        module.setDescription(request.description());

        var savedModule = moduleRepository.save(module);
        return moduleMapper.toResponse(savedModule);
    }

    @CacheEvict(key = "#moduleId")
    public void deleteById(
            @UUID @NotNull String moduleId
    ) throws NoSuchElementException {
        var module = findById(moduleId);
        moduleRepository.delete(module);
    }

    @NotNull
    public Module findById(@UUID @NotNull String moduleId)
            throws NoSuchElementException {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> notFoundHandler(moduleId));
    }

    private NoSuchElementException notFoundHandler(String moduleId) {
        var message = messageSource.getMessage(
                "module.not_found",
                new Object[]{moduleId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
