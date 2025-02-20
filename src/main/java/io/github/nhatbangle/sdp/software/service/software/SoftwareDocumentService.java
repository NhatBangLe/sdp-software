package io.github.nhatbangle.sdp.software.service.software;

import io.github.nhatbangle.sdp.software.dto.*;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentCreateRequest;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentResponse;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.composite.SoftwareDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocumentHasAttachment;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.software.SoftwareDocumentMapper;
import io.github.nhatbangle.sdp.software.repository.software.SoftwareDocumentRepository;
import io.github.nhatbangle.sdp.software.service.DocumentTypeService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Validated
@CacheConfig(cacheNames = "sdp_software-software_document")
@RequiredArgsConstructor
public class SoftwareDocumentService {

    private final MessageSource messageSource;
    private final SoftwareDocumentRepository softwareDocumentRepository;
    private final SoftwareDocumentMapper softwareDocumentMapper;
    private final SoftwareVersionService softwareVersionService;
    private final DocumentTypeService documentTypeService;

    @NotNull
    public PagingWrapper<SoftwareDocumentResponse> getAllByVersionId(
            @UUID @NotNull String softwareVersionId,
            @Nullable String documentTypeName,
            @Nullable String documentName,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var page = softwareDocumentRepository
                .findAllByVersion_IdAndType_NameContainsIgnoreCaseAndNameContainsIgnoreCase(
                        softwareVersionId,
                        Objects.requireNonNullElse(documentTypeName, ""),
                        Objects.requireNonNullElse(documentName, ""),
                        pageable
                ).map(softwareDocumentMapper::toResponse);
        return PagingWrapper.fromPage(page);
    }

    @NotNull
    @Cacheable(key = "#documentId")
    public SoftwareDocumentResponse getById(
            @UUID @NotNull String documentId
    ) throws NoSuchElementException {
        var document = softwareDocumentRepository.findInfoById(documentId)
                .orElseThrow(() -> notFoundHandler(documentId));
        return softwareDocumentMapper.toResponse(document);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public SoftwareDocumentResponse create(
            @UUID @NotNull String versionId,
            @NotNull @Valid SoftwareDocumentCreateRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var version = softwareVersionService.findById(versionId);
        var type = documentTypeService.findById(request.typeId());

        var document = softwareDocumentRepository.save(SoftwareDocument.builder()
                .name(request.name())
                .description(request.description())
                .version(version)
                .type(type)
                .build());
        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            var documentId = document.getId();
            var attachments = attachmentIds.stream()
                    .map(atmId -> SoftwareDocumentHasAttachment.builder()
                            .id(SoftwareDocumentHasAttachmentId.builder()
                                    .attachmentId(atmId)
                                    .documentId(documentId)
                                    .build())
                            .build()
                    ).collect(Collectors.toSet());
            document.setAttachments(attachments);
            document = softwareDocumentRepository.save(document);
        }

        return softwareDocumentMapper.toResponse(document);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#documentId")
    public SoftwareDocumentResponse updateById(
            @UUID @NotNull String documentId,
            @NotNull @Valid SoftwareDocumentUpdateRequest request
    ) throws NoSuchElementException {
        var document = findById(documentId);

        document.setName(request.name());
        document.setDescription(request.description());
        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            var attachments = attachmentIds.stream()
                    .map(atmId -> SoftwareDocumentHasAttachment.builder()
                            .id(SoftwareDocumentHasAttachmentId.builder()
                                    .attachmentId(atmId)
                                    .documentId(documentId)
                                    .build())
                            .build()
                    ).collect(Collectors.toSet());
            document.setAttachments(attachments);
        }

        var savedDocument = softwareDocumentRepository.save(document);
        return softwareDocumentMapper.toResponse(savedDocument);
    }

    @CacheEvict(key = "#documentId")
    public void deleteById(
            @UUID @NotNull String documentId
    ) throws NoSuchElementException {
        var document = findById(documentId);
        softwareDocumentRepository.delete(document);
    }

    @NotNull
    public SoftwareDocument findById(@UUID @NotNull String documentId)
            throws NoSuchElementException {
        return softwareDocumentRepository.findById(documentId)
                .orElseThrow(() -> notFoundHandler(documentId));
    }

    private NoSuchElementException notFoundHandler(String documentId) {
        var message = messageSource.getMessage(
                "software_document.not_found",
                new Object[]{documentId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
