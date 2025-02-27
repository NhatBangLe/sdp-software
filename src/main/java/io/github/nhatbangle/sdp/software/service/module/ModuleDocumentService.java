package io.github.nhatbangle.sdp.software.service.module;

import io.github.nhatbangle.sdp.software.dto.AttachmentUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.document.*;
import io.github.nhatbangle.sdp.software.entity.Attachment;
import io.github.nhatbangle.sdp.software.entity.composite.ModuleDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.module.ModuleDocument;
import io.github.nhatbangle.sdp.software.entity.module.ModuleDocumentHasAttachment;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleDocumentMapper;
import io.github.nhatbangle.sdp.software.repository.module.ModuleDocumentHasAttachmentRepository;
import io.github.nhatbangle.sdp.software.repository.module.ModuleDocumentRepository;
import io.github.nhatbangle.sdp.software.service.AttachmentService;
import io.github.nhatbangle.sdp.software.service.DocumentTypeService;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Sort;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Validated
@CacheConfig(cacheNames = "sdp_software-module_document")
@RequiredArgsConstructor
public class ModuleDocumentService {

    private final MessageSource messageSource;
    private final ModuleDocumentMapper mapper;
    private final DocumentTypeService documentTypeService;
    private final AttachmentService atmService;
    private final ModuleDocumentRepository repository;
    private final ModuleDocumentHasAttachmentRepository docAtmRepository;
    private final ModuleVersionService moduleVersionService;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<ModuleDocumentResponse> getAllByVersionId(
            @UUID @NotNull String moduleVersionId,
            @Nullable String documentTypeName,
            @Nullable String documentName,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var page = repository.findAllByVersion_IdAndType_NameContainsIgnoreCaseAndNameContainsIgnoreCase(
                moduleVersionId,
                Objects.requireNonNullElse(documentTypeName, ""),
                Objects.requireNonNullElse(documentName, ""),
                pageable
        ).map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#documentId")
    public ModuleDocumentResponse getById(
            @UUID @NotNull String documentId
    ) throws NoSuchElementException {
        var document = repository.findInfoById(documentId)
                .orElseThrow(() -> notFoundHandler(documentId));
        return mapper.toResponse(document);
    }

    @NotNull
    @Transactional(readOnly = true)
    public List<String> getAttachments(@NotNull @UUID String documentId) {
        return docAtmRepository.findAllById_DocumentId(
                        documentId, Sort.by("createdAt").ascending())
                .map(atm -> atm.getId().getAttachmentId())
                .toList();
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public ModuleDocumentResponse create(
            @UUID @NotNull String versionId,
            @NotNull @Valid ModuleDocumentCreateRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var version = moduleVersionService.findById(versionId);
        var type = documentTypeService.findById(request.typeId());

        var document = repository.save(ModuleDocument.builder()
                .name(request.name())
                .description(request.description())
                .version(version)
                .type(type)
                .build());
        return mapper.toResponse(document);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#documentId")
    public ModuleDocumentResponse updateById(
            @UUID @NotNull String documentId,
            @NotNull @Valid ModuleDocumentUpdateRequest request
    ) throws NoSuchElementException {
        var document = findById(documentId);
        document.setName(request.name());
        document.setDescription(request.description());

        var savedDocument = repository.save(document);
        return mapper.toResponse(savedDocument);
    }

    @Transactional
    public void updateAttachment(
            @UUID @NotNull String documentId,
            @NotNull @Valid AttachmentUpdateRequest request
    ) throws NoSuchElementException, IllegalArgumentException, ServiceUnavailableException {
        var document = findById(documentId);
        var attachmentId = request.attachmentId();
        var result = atmService.isFileExist(attachmentId);
        atmService.foundOrElseThrow(attachmentId, result);

        var id = ModuleDocumentHasAttachmentId.builder()
                .attachmentId(request.attachmentId())
                .documentId(documentId)
                .build();
        switch (request.operator()) {
            case ADD -> {
                if (docAtmRepository.existsById_DocumentIdAndId_AttachmentId(documentId, attachmentId)) {
                    var message = messageSource.getMessage(
                            "module_document.attachment_already_exists",
                            new Object[]{attachmentId, documentId},
                            Locale.getDefault()
                    );
                    throw new IllegalArgumentException(message);
                }
                var attachment = ModuleDocumentHasAttachment.builder()
                        .id(id)
                        .attachment(Attachment.builder().id(request.attachmentId()).build())
                        .document(document)
                        .build();
                docAtmRepository.save(attachment);
            }
            case REMOVE -> docAtmRepository.deleteById(id);
        }
    }

    @CacheEvict(key = "#documentId")
    public void deleteById(
            @UUID @NotNull String documentId
    ) throws NoSuchElementException {
        var document = findById(documentId);
        repository.delete(document);
    }

    @NotNull
    public ModuleDocument findById(@UUID @NotNull String documentId)
            throws NoSuchElementException {
        return repository.findById(documentId)
                .orElseThrow(() -> notFoundHandler(documentId));
    }

    private NoSuchElementException notFoundHandler(String documentId) {
        var message = messageSource.getMessage(
                "module_document.not_found",
                new Object[]{documentId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
