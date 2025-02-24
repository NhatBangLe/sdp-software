package io.github.nhatbangle.sdp.software.service.deployment;

import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.deployment.*;
import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.deployment.DeploymentPhaseTypeMapper;
import io.github.nhatbangle.sdp.software.repository.UserRepository;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentPhaseTypeRepository;
import io.github.nhatbangle.sdp.software.service.UserService;
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
@RequiredArgsConstructor
@CacheConfig(cacheNames = "sdp_software-deployment_phase_type")
public class DeploymentPhaseTypeService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final DeploymentPhaseTypeRepository repository;
    private final DeploymentPhaseTypeMapper mapper;
    private final UserRepository userRepository;

    @NotNull
    public PagingWrapper<DeploymentPhaseTypeResponse> getAll(
            @UUID @NotNull String userId,
            @Nullable String name,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository.findAllByUser_IdAndNameContainsIgnoreCase(
                userId,
                Objects.requireNonNullElse(name, ""),
                pageable
        ).map(mapper::toResponse);
        return PagingWrapper.fromPage(page);
    }

    @NotNull
    @Cacheable(key = "#phaseId")
    public DeploymentPhaseTypeResponse getById(
            @UUID @NotNull String phaseId
    ) throws NoSuchElementException {
        var type = repository.findInfoById(phaseId)
                .orElseThrow(() -> notFoundHandler(phaseId));
        return mapper.toResponse(type);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public DeploymentPhaseTypeResponse create(
            @UUID @NotNull String userId,
            @NotNull @Valid DeploymentPhaseTypeCreateRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var user = userRepository.findById(userId).orElseGet(() -> {
            var result = userService.validateUserId(userId);
            userService.foundOrElseThrow(userId, result);
            return User.builder().id(userId).build();
        });

        var type = repository.save(DeploymentPhaseType.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .build());
        return mapper.toResponse(type);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#typeId")
    public DeploymentPhaseTypeResponse updateById(
            @UUID @NotNull String typeId,
            @NotNull @Valid DeploymentPhaseTypeUpdateRequest request
    ) throws NoSuchElementException {
        var type = findById(typeId);
        type.setName(request.name());
        type.setDescription(request.description());

        var savedType = repository.save(type);
        return mapper.toResponse(savedType);
    }

    @CacheEvict(key = "#typeId")
    public void deleteById(
            @UUID @NotNull String typeId
    ) throws NoSuchElementException {
        var type = findById(typeId);
        repository.delete(type);
    }

    @NotNull
    public DeploymentPhaseType findById(@UUID @NotNull String typeId)
            throws NoSuchElementException {
        return repository.findById(typeId).orElseThrow(() -> notFoundHandler(typeId));
    }

    private NoSuchElementException notFoundHandler(String typeId) {
        var message = messageSource.getMessage(
                "deployment_phase_type.not_found",
                new Object[]{typeId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
