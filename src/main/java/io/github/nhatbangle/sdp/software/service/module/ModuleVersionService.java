package io.github.nhatbangle.sdp.software.service.module;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.module.*;
import io.github.nhatbangle.sdp.software.entity.module.ModuleVersion;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleVersionMapper;
import io.github.nhatbangle.sdp.software.repository.module.ModuleVersionRepository;
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
@CacheConfig(cacheNames = "sdp_software-module_version")
@RequiredArgsConstructor
public class ModuleVersionService {

    private final MessageSource messageSource;
    private final ModuleVersionMapper moduleVersionMapper;
    private final ModuleVersionRepository moduleVersionRepository;
    private final ModuleService moduleService;

    @NotNull
    public PagingWrapper<ModuleVersionResponse> getAllByModuleId(
            @UUID @NotNull String moduleId,
            @Nullable String name,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = moduleVersionRepository
                .findAllByModule_IdAndNameContainsIgnoreCase(
                        moduleId,
                        Objects.requireNonNullElse(name, ""),
                        pageable
                )
                .map(moduleVersionMapper::toResponse);
        return PagingWrapper.fromPage(page);
    }

    @NotNull
    @Cacheable(key = "#versionId")
    public ModuleVersionResponse getById(
            @UUID @NotNull String versionId
    ) throws NoSuchElementException {
        var version = moduleVersionRepository.findInfoById(versionId)
                .orElseThrow(() -> notFoundHandler(versionId));
        return moduleVersionMapper.toResponse(version);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public ModuleVersionResponse create(
            @UUID @NotNull String moduleId,
            @NotNull @Valid ModuleVersionCreateRequest request
    ) throws IllegalArgumentException {
        var module = moduleService.findById(moduleId);
        var version = moduleVersionRepository.save(ModuleVersion.builder()
                .name(request.name())
                .description(request.description())
                .module(module)
                .build());
        return moduleVersionMapper.toResponse(version);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#versionId")
    public ModuleVersionResponse updateById(
            @UUID @NotNull String versionId,
            @NotNull @Valid ModuleVersionUpdateRequest request
    ) throws NoSuchElementException {
        var version = findById(versionId);
        version.setName(request.name());
        version.setDescription(request.description());

        var savedVersion = moduleVersionRepository.save(version);
        return moduleVersionMapper.toResponse(savedVersion);
    }

    @CacheEvict(key = "#moduleId")
    public void deleteById(
            @UUID @NotNull String moduleId
    ) throws NoSuchElementException {
        var module = findById(moduleId);
        moduleVersionRepository.delete(module);
    }

    @NotNull
    public ModuleVersion findById(@UUID @NotNull String moduleId)
            throws NoSuchElementException {
        return moduleVersionRepository.findById(moduleId)
                .orElseThrow(() -> notFoundHandler(moduleId));
    }

    private NoSuchElementException notFoundHandler(String versionId) {
        var message = messageSource.getMessage(
                "module_version.not_found",
                new Object[]{versionId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
