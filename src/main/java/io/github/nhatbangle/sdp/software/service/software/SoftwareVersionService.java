package io.github.nhatbangle.sdp.software.service.software;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.dto.*;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareNameAndVersionResponse;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareVersionCreateRequest;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareVersionResponse;
import io.github.nhatbangle.sdp.software.dto.software.SoftwareVersionUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion;
import io.github.nhatbangle.sdp.software.mapper.software.SoftwareVersionMapper;
import io.github.nhatbangle.sdp.software.repository.software.SoftwareVersionRepository;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

@Service
@Validated
@CacheConfig(cacheNames = CacheName.SOFTWARE_VERSION)
@RequiredArgsConstructor
public class SoftwareVersionService {

    private final MessageSource messageSource;
    private final SoftwareVersionRepository repository;
    private final SoftwareVersionMapper mapper;
    private final SoftwareService softwareService;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<SoftwareNameAndVersionResponse> getAllByUserId(
            @UUID @NotNull String userId,
            @Nullable String softwareName,
            @Nullable String versionName,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var page = repository
                .findAllBySoftware_User_IdAndSoftware_NameContainsIgnoreCaseAndNameContainsIgnoreCase(
                        userId,
                        Objects.requireNonNullElse(softwareName, ""),
                        Objects.requireNonNullElse(versionName, ""),
                        pageable
                ).map(mapper::toResponse);
        var wrapper = PagingWrapper.from(page);
        wrapper.sort((o1, o2) -> {
            var softName1 = o1.softwareName();
            var softName2 = o2.softwareName();
            var verName1 = o1.versionName();
            var verName2 = o2.versionName();

            if (softName1.equals(softName2)) return verName1.compareTo(verName2);
            return softName1.compareTo(softName2);
        });
        return wrapper;
    }

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<SoftwareVersionResponse> getAllBySoftwareId(
            @UUID @NotNull String softwareId,
            @Nullable String name,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository
                .findAllBySoftware_IdAndNameContainsIgnoreCase(
                        softwareId,
                        Objects.requireNonNullElse(name, ""),
                        pageable
                )
                .map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#versionId")
    public SoftwareVersionResponse getById(
            @UUID @NotNull String versionId
    ) throws NoSuchElementException {
        var software = repository.findInfoById(versionId)
                .orElseThrow(() -> notFoundHandler(versionId));
        return mapper.toResponse(software);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public SoftwareVersionResponse create(
            @UUID @NotNull String softwareId,
            @NotNull @Valid SoftwareVersionCreateRequest request
    ) throws NoSuchElementException {
        var software = softwareService.findById(softwareId);
        var version = repository.save(SoftwareVersion.builder()
                .name(request.name())
                .description(request.description())
                .software(software)
                .build());
        return mapper.toResponse(version);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#versionId")
    public SoftwareVersionResponse updateById(
            @UUID @NotNull String versionId,
            @NotNull @Valid SoftwareVersionUpdateRequest request
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
    public SoftwareVersion findById(@UUID @NotNull String versionId)
            throws NoSuchElementException {
        return repository.findById(versionId)
                .orElseThrow(() -> notFoundHandler(versionId));
    }

    private NoSuchElementException notFoundHandler(String versionId) {
        var message = messageSource.getMessage(
                "software_version.not_found",
                new Object[]{versionId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
