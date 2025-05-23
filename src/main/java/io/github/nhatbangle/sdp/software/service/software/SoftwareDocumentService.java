package io.github.nhatbangle.sdp.software.service.software;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.dto.AttachmentUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentCreateRequest;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentResponse;
import io.github.nhatbangle.sdp.software.dto.document.SoftwareDocumentUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.Attachment;
import io.github.nhatbangle.sdp.software.entity.composite.SoftwareDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument;
import io.github.nhatbangle.sdp.software.entity.software.SoftwareDocumentHasAttachment;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.software.SoftwareDocumentMapper;
import io.github.nhatbangle.sdp.software.repository.software.SoftwareDocumentHasAttachmentRepository;
import io.github.nhatbangle.sdp.software.repository.software.SoftwareDocumentRepository;
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
@CacheConfig(cacheNames = CacheName.SOFTWARE_DOCUMENT)
@RequiredArgsConstructor
public class SoftwareDocumentService {

    private final MessageSource messageSource;
    private final SoftwareDocumentRepository repository;
    private final SoftwareDocumentMapper mapper;
    private final SoftwareVersionService softwareVersionService;
    private final DocumentTypeService documentTypeService;
    private final SoftwareDocumentHasAttachmentRepository docAtmRepository;
    private final AttachmentService atmService;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<SoftwareDocumentResponse> getAllByVersionId(
            @UUID @NotNull String softwareVersionId,
            @Nullable String documentTypeName,
            @Nullable String documentName,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var page = repository
                .findAllByVersion_IdAndType_NameContainsIgnoreCaseAndNameContainsIgnoreCase(
                        softwareVersionId,
                        Objects.requireNonNullElse(documentTypeName, ""),
                        Objects.requireNonNullElse(documentName, ""),
                        pageable
                ).map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#documentId")
    public SoftwareDocumentResponse getById(
            @UUID @NotNull String documentId
    ) throws NoSuchElementException {
        var document = repository.findInfoById(documentId)
                .orElseThrow(() -> notFoundHandler(documentId));
        return mapper.toResponse(document);
    }

    @NotNull
    @Transactional(readOnly = true)
    public List<String> getAllAttachments(@NotNull @UUID String documentId) {
        return docAtmRepository.findAllById_DocumentId(
                        documentId, Sort.by("createdAt").ascending())
                .map(atm -> atm.getId().getAttachmentId())
                .toList();
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

        var document = repository.save(SoftwareDocument.builder()
                .name(request.name())
                .description(request.description())
                .version(version)
                .type(type)
                .build());
        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            var documentId = document.getId();
            var attachments = attachmentIds.stream().map(atmId -> {
                var id = SoftwareDocumentHasAttachmentId.builder()
                        .attachmentId(atmId)
                        .documentId(documentId)
                        .build();
                return SoftwareDocumentHasAttachment.builder()
                        .id(id)
                        .attachment(Attachment.builder().id(atmId).build())
                        .document(document)
                        .build();
            }).toList();
            docAtmRepository.saveAll(attachments);
        }
        return mapper.toResponse(document);
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

        var savedDocument = repository.save(document);
        return mapper.toResponse(savedDocument);
    }

    @Transactional
    public void updateAttachmentById(
            @UUID @NotNull String documentId,
            @NotNull @Valid AttachmentUpdateRequest request
    ) throws NoSuchElementException, IllegalArgumentException, ServiceUnavailableException {
        var document = findById(documentId);
        var attachmentId = request.attachmentId();
        var result = atmService.isFileExist(attachmentId);
        atmService.foundOrElseThrow(attachmentId, result);

        var id = SoftwareDocumentHasAttachmentId.builder()
                .attachmentId(request.attachmentId())
                .documentId(documentId)
                .build();
        switch (request.operator()) {
            case ADD -> {
                if (docAtmRepository.existsById_DocumentIdAndId_AttachmentId(documentId, attachmentId)) {
                    var message = messageSource.getMessage(
                            "software_document.attachment_already_exists",
                            new Object[]{attachmentId, documentId},
                            Locale.getDefault()
                    );
                    throw new IllegalArgumentException(message);
                }
                var attachment = SoftwareDocumentHasAttachment.builder()
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
    ) {
        repository.deleteById(documentId);
    }

    @NotNull
    public SoftwareDocument findById(@UUID @NotNull String documentId)
            throws NoSuchElementException {
        return repository.findById(documentId)
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
