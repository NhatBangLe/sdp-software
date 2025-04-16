package io.github.nhatbangle.sdp.software.service.module;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.module.*;
import io.github.nhatbangle.sdp.software.entity.module.ModuleVersion;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleVersionMapper;
import io.github.nhatbangle.sdp.software.repository.module.ModuleVersionRepository;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;
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
@CacheConfig(cacheNames = CacheName.MODULE_VERSION)
@RequiredArgsConstructor
public class ModuleVersionService {

    private final MessageSource messageSource;
    private final ModuleVersionMapper mapper;
    private final ModuleVersionRepository repository;
    private final ModuleService moduleService;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<ModuleNameAndVersionResponse> getAllBySoftwareVersionId(
            @UUID @NotNull String softwareVersionId,
            @Nullable String moduleName,
            @Nullable String versionName,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var page = repository
                .findAllByModule_SoftwareVersion_IdAndModule_NameContainsIgnoreCaseAndNameContainsIgnoreCase(
                        softwareVersionId,
                        Objects.requireNonNullElse(moduleName, ""),
                        Objects.requireNonNullElse(versionName, ""),
                        pageable
                )
                .map(mapper::toResponse);
        var wrapper = PagingWrapper.from(page);
        wrapper.sort((o1, o2) -> {
            var moduleName1 = o1.moduleName();
            var moduleName2 = o2.moduleName();
            var verName1 = o1.versionName();
            var verName2 = o2.versionName();

            if (moduleName1.equals(moduleName2)) return verName1.compareTo(verName2);
            return moduleName1.compareTo(moduleName2);
        });
        return wrapper;
    }

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<ModuleVersionResponse> getAllByModuleId(
            @UUID @NotNull String moduleId,
            @Nullable String name,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository
                .findAllByModule_IdAndNameContainsIgnoreCase(
                        moduleId,
                        Objects.requireNonNullElse(name, ""),
                        pageable
                )
                .map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#versionId")
    public ModuleVersionResponse getById(
            @UUID @NotNull String versionId
    ) throws NoSuchElementException {
        var version = repository.findInfoById(versionId)
                .orElseThrow(() -> notFoundHandler(versionId));
        return mapper.toResponse(version);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public ModuleVersionResponse create(
            @UUID @NotNull String moduleId,
            @NotNull @Valid ModuleVersionCreateRequest request
    ) throws IllegalArgumentException {
        var module = moduleService.findById(moduleId);
        var version = repository.save(ModuleVersion.builder()
                .name(request.name())
                .description(request.description())
                .module(module)
                .build());
        return mapper.toResponse(version);
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

        var savedVersion = repository.save(version);
        return mapper.toResponse(savedVersion);
    }

    @CacheEvict(key = "#versionId")
    public void deleteById(
            @UUID @NotNull String versionId
    ) {
        repository.deleteById(versionId);
    }

    @NotNull
    public ModuleVersion findById(@UUID @NotNull String versionId)
            throws NoSuchElementException {
        return repository.findById(versionId)
                .orElseThrow(() -> notFoundHandler(versionId));
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
