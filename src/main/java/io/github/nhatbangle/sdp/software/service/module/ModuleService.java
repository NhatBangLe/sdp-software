package io.github.nhatbangle.sdp.software.service.module;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.module.ModuleCreateRequest;
import io.github.nhatbangle.sdp.software.dto.module.ModuleResponse;
import io.github.nhatbangle.sdp.software.dto.module.ModuleUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.module.Module;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleMapper;
import io.github.nhatbangle.sdp.software.repository.module.ModuleRepository;
import io.github.nhatbangle.sdp.software.service.software.SoftwareVersionService;
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
@CacheConfig(cacheNames = CacheName.MODULE)
@RequiredArgsConstructor
public class ModuleService {

    private final MessageSource messageSource;
    private final SoftwareVersionService softwareVersionService;
    private final ModuleRepository repository;
    private final ModuleMapper mapper;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<ModuleResponse> getAllByVersionId(
            @UUID @NotNull String softwareVersionId,
            @Nullable String name,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository
                .findAllBySoftwareVersion_IdAndNameContainsIgnoreCase(
                        softwareVersionId,
                        Objects.requireNonNullElse(name, ""),
                        pageable
                )
                .map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#moduleId")
    public ModuleResponse getById(
            @UUID @NotNull String moduleId
    ) throws NoSuchElementException {
        var module = repository.findInfoById(moduleId)
                .orElseThrow(() -> notFoundHandler(moduleId));
        return mapper.toResponse(module);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public ModuleResponse create(
            @UUID @NotNull String softwareVersionId,
            @NotNull @Valid ModuleCreateRequest request
    ) throws IllegalArgumentException {
        var softwareVersion = softwareVersionService.findById(softwareVersionId);
        var module = repository.save(Module.builder()
                .name(request.name())
                .description(request.description())
                .softwareVersion(softwareVersion)
                .build());
        return mapper.toResponse(module);
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

        var savedModule = repository.save(module);
        return mapper.toResponse(savedModule);
    }

    @CacheEvict(key = "#moduleId")
    public void deleteById(
            @UUID @NotNull String moduleId
    ) {
        repository.deleteById(moduleId);
    }

    @NotNull
    public Module findById(@UUID @NotNull String moduleId)
            throws NoSuchElementException {
        return repository.findById(moduleId)
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
