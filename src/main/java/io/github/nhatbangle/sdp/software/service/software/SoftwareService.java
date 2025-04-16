package io.github.nhatbangle.sdp.software.service.software;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareCreateRequest;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareResponse;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.entity.software.Software;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.software.SoftwareMapper;
import io.github.nhatbangle.sdp.software.repository.UserRepository;
import io.github.nhatbangle.sdp.software.repository.software.SoftwareRepository;
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
@CacheConfig(cacheNames = CacheName.SOFTWARE)
@RequiredArgsConstructor
public class SoftwareService {

    private final SoftwareRepository repository;
    private final MessageSource messageSource;
    private final SoftwareMapper mapper;
    private final UserService userService;
    private final UserRepository userRepository;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<SoftwareResponse> getAllByUserId(
            @UUID @NotNull String userId,
            @Nullable String name,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository
                .findAllByUser_IdAndNameContainsIgnoreCase(
                        userId,
                        Objects.requireNonNullElse(name, ""),
                        pageable
                )
                .map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#softwareId")
    public SoftwareResponse getById(
            @UUID @NotNull String softwareId
    ) throws NoSuchElementException {
        var software = repository.findInfoById(softwareId)
                .orElseThrow(() -> notFoundHandler(softwareId));
        return mapper.toResponse(software);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public SoftwareResponse create(
            @UUID @NotNull String userId,
            @NotNull @Valid SoftwareCreateRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var user = userRepository.findById(userId).orElseGet(() -> {
            var result = userService.validateUserId(userId);
            userService.foundOrElseThrow(userId, result);
            return User.builder().id(userId).build();
        });

        var software = repository.save(Software.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .build());
        return mapper.toResponse(software);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#softwareId")
    public SoftwareResponse updateById(
            @UUID @NotNull String softwareId,
            @NotNull @Valid SoftwareUpdateRequest request
    ) throws NoSuchElementException {
        var software = findById(softwareId);
        software.setName(request.name());
        software.setDescription(request.description());

        var savedSoftware = repository.save(software);
        return mapper.toResponse(savedSoftware);
    }

    @CacheEvict(key = "#softwareId")
    public void deleteById(
            @UUID @NotNull String softwareId
    ) {
        repository.deleteById(softwareId);
    }

    @NotNull
    public Software findById(@UUID @NotNull String softwareId)
            throws NoSuchElementException {
        return repository.findById(softwareId)
                .orElseThrow(() -> notFoundHandler(softwareId));
    }

    private NoSuchElementException notFoundHandler(String softwareId) {
        var message = messageSource.getMessage(
                "software.not_found",
                new Object[]{softwareId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
