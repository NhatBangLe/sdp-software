package io.github.nhatbangle.sdp.software.service;

import io.github.nhatbangle.sdp.software.dto.*;
import io.github.nhatbangle.sdp.software.dto.document.DocumentTypeCreateRequest;
import io.github.nhatbangle.sdp.software.dto.document.DocumentTypeResponse;
import io.github.nhatbangle.sdp.software.dto.document.DocumentTypeUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.DocumentType;
import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.DocumentTypeMapper;
import io.github.nhatbangle.sdp.software.repository.DocumentTypeRepository;
import io.github.nhatbangle.sdp.software.repository.UserRepository;
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
@CacheConfig(cacheNames = "sdp_software-document_type")
@RequiredArgsConstructor
public class DocumentTypeService {

    private final MessageSource messageSource;
    private final DocumentTypeRepository repository;
    private final DocumentTypeMapper mapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public PagingWrapper<DocumentTypeResponse> getAll(
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

    @Cacheable(key = "#typeId")
    public DocumentTypeResponse getById(
            @UUID @NotNull String typeId
    ) throws NoSuchElementException {
        var type = repository.findInfoById(typeId)
                .orElseThrow(() -> notFoundHandler(typeId));
        return mapper.toResponse(type);
    }

    @Transactional
    @CachePut(key = "#result.id()")
    public DocumentTypeResponse create(
            @UUID @NotNull String userId,
            @NotNull @Valid DocumentTypeCreateRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var user = userRepository.findById(userId).orElseGet(() -> {
            var result = userService.validateUserId(userId);
            userService.foundOrElseThrow(userId, result);
            return User.builder().id(userId).build();
        });

        var type = repository.save(DocumentType.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .build());
        return mapper.toResponse(type);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#typeId")
    public DocumentTypeResponse updateById(
            @UUID @NotNull String typeId,
            @NotNull @Valid DocumentTypeUpdateRequest request
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
    public DocumentType findById(@UUID @NotNull String typeId)
            throws NoSuchElementException {
        return repository.findById(typeId)
                .orElseThrow(() -> notFoundHandler(typeId));
    }

    private NoSuchElementException notFoundHandler(String typeId) {
        var message = messageSource.getMessage(
                "document_type.not_found",
                new Object[]{typeId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
