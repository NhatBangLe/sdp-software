package io.github.nhatbangle.sdp.software.service.deployment;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import io.github.nhatbangle.sdp.software.constant.MailTemplatePlaceholder;
import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import io.github.nhatbangle.sdp.software.dto.deployment.*;
import io.github.nhatbangle.sdp.software.dto.notification.MailSendPayload;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.entity.MailTemplate;
import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentProcessHasModuleVersionId;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentProcessHasUserId;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasModuleVersion;
import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasUser;
import io.github.nhatbangle.sdp.software.mapper.deployment.DeploymentProcessMapper;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentProcessHasModuleVersionRepository;
import io.github.nhatbangle.sdp.software.repository.UserRepository;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentProcessHasUserRepository;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentProcessRepository;
import io.github.nhatbangle.sdp.software.service.CustomerService;
import io.github.nhatbangle.sdp.software.service.MailTemplateService;
import io.github.nhatbangle.sdp.software.service.UserService;
import io.github.nhatbangle.sdp.software.service.module.ModuleVersionService;
import io.github.nhatbangle.sdp.software.service.software.SoftwareVersionService;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheName.DEPLOYMENT_PROCESS)
public class DeploymentProcessService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final DeploymentProcessRepository repository;
    private final DeploymentProcessMapper mapper;
    private final SoftwareVersionService softwareVersionService;
    private final CustomerService customerService;
    private final DeploymentProcessHasUserRepository processHasUserRepository;
    private final DeploymentProcessHasModuleVersionRepository processHasModuleVersionsRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MailTemplateService mailTemplateService;
    private final ModuleVersionService moduleVersionService;

    @Value("${app.mail-box-queue}")
    private String mailBoxQueue;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<DeploymentProcessResponse> getAll(
            @UUID @NotNull String creatorId,
            @Nullable String softwareName,
            @Nullable String customerName,
            @Nullable DeploymentProcessStatus status,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository
                .findAllByCreator_IdAndSoftwareVersion_Software_NameContainsIgnoreCaseAndCustomer_NameContainsIgnoreCaseAndStatus(
                        creatorId,
                        Objects.requireNonNullElse(softwareName, ""),
                        Objects.requireNonNullElse(customerName, ""),
                        status,
                        pageable
                ).map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<DeploymentProcessHasSoftwareVersionResponse> getAllByCustomerId(
            @NotNull @UUID String customerId,
            @Nullable String softwareName,
            @Nullable String softwareVersionName,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository
                .findByCustomer_IdAndSoftwareVersion_Software_NameContainsIgnoreCaseAndSoftwareVersion_NameContainsIgnoreCase(
                        customerId,
                        Objects.requireNonNullElse(softwareName, ""),
                        Objects.requireNonNullElse(softwareVersionName, ""),
                        pageable
                ).map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#processId")
    public DeploymentProcessResponse getById(
            @Min(0) @NotNull Long processId
    ) throws NoSuchElementException {
        var process = repository.findInfoById(processId)
                .orElseThrow(() -> notFoundHandler(processId));
        return mapper.toResponse(process);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheName.DEPLOYMENT_PROCESS_MODULE, key = "#processId")
    public List<DeploymentProcessHasModuleVersionResponse> getAllModuleVersions(
            @Min(0) @NotNull Long processId
    ) throws NoSuchElementException {
        var members = processHasModuleVersionsRepository
                .findAllByProcess_Id(processId, Sort.by("createdAt").ascending());
        return members.map(mapper::toResponse).toList();
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheName.DEPLOYMENT_PROCESS_MEMBER, key = "#processId")
    public List<String> getAllMembers(
            @Min(0) @NotNull Long processId
    ) throws NoSuchElementException {
        var members = processHasUserRepository
                .findAllById_ProcessId(processId, Sort.by("createdAt").ascending());
        return members.map(member -> member.getId().getUserId()).toList();
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public DeploymentProcessResponse create(
            @UUID @NotNull String userId,
            @NotNull @Valid DeploymentProcessCreateRequest request
    ) throws NoSuchElementException {
        var user = userService.getById(userId);
        var version = softwareVersionService.findById(request.softwareVersionId());
        var customer = customerService.findById(request.customerId());
        var moduleVersions = request.moduleVersionIds().stream().map(moduleVersionService::findById);

        var process = repository.save(DeploymentProcess.builder()
                .softwareVersion(version)
                .customer(customer)
                .creator(user)
                .build());

        var processId = process.getId();
        processHasModuleVersionsRepository.saveAll(moduleVersions.map(moduleVersion -> {
            var id = DeploymentProcessHasModuleVersionId.builder()
                    .processId(processId)
                    .versionId(moduleVersion.getId())
                    .build();
            return DeploymentProcessHasModuleVersion.builder()
                    .id(id)
                    .process(process)
                    .version(moduleVersion)
                    .build();
        }).toList());

        return mapper.toResponse(process);
    }

    @Transactional(readOnly = true)
    protected void sendProcessDoneAlertMail(
            @NotNull MailTemplate template,
            @NotNull DeploymentProcess process
    ) {
        var charset = MailTemplateService.DEFAULT_CHARSET;
        var customer = process.getCustomer();
        var softwareVersion = process.getSoftwareVersion();
        var software = softwareVersion.getSoftware();
        var content = new String(template.getContent(), charset)
                .replace(MailTemplatePlaceholder.CUSTOMER_NAME.name(), customer.getName())
                .replace(MailTemplatePlaceholder.DEPLOYMENT_PROCESS_ID.name(), process.getId().toString())
                .replace(MailTemplatePlaceholder.SOFTWARE_NAME.name(), software.getName())
                .replace(MailTemplatePlaceholder.SOFTWARE_VERSION.name(), softwareVersion.getName());

        var payload = new MailSendPayload(
                template.getSubject(),
                content.getBytes(charset),
                charset.name(),
                customer.getEmail()
        );
        rabbitTemplate.convertAndSend(mailBoxQueue, payload);
    }

    @Transactional
    @CacheEvict(cacheNames = CacheName.DEPLOYMENT_PROCESS_MEMBER, key = "#processId")
    public void updateMember(
            @Min(0) @NotNull Long processId,
            @NotNull @Valid DeploymentProcessMemberUpdateRequest request
    ) throws NoSuchElementException, IllegalArgumentException {
        var process = findById(processId);
        switch (request.operator()) {
            case ADD -> {
                var member = memberIdToEntity(request.memberId(), process);
                processHasUserRepository.save(member);
            }
            case REMOVE -> processHasUserRepository
                    .deleteById_ProcessIdAndId_UserId(processId, request.memberId());
        }
        repository.save(process);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#processId")
    public DeploymentProcessResponse updateById(
            @Min(0) @NotNull Long processId,
            @NotNull @Valid DeploymentProcessUpdateRequest request
    ) throws NoSuchElementException {
        var process = findById(processId);
        var status = request.status();
        process.setStatus(status);
        if (status == DeploymentProcessStatus.DONE) {
            var creator = process.getCreator();
            try {
                var mailTemplate = mailTemplateService
                        .findByUserIdAndType(creator.getId(), MailTemplateType.SOFTWARE_DEPLOYED_SUCCESSFULLY);
                sendProcessDoneAlertMail(mailTemplate, process);
            } catch (NoSuchElementException e) {
                log.warn("Could not find mail template for process with id {}", processId);
                log.debug(e.getMessage(), e);
            }
        }

        var savedProcess = repository.save(process);
        return mapper.toResponse(savedProcess);
    }

    @CacheEvict(key = "#processId")
    public void deleteById(
            @Min(0) @NotNull Long processId
    ) throws NoSuchElementException {
        repository.deleteById(processId);
    }

    @NotNull
    public DeploymentProcess findById(@Min(0) @NotNull Long processId)
            throws NoSuchElementException {
        return repository.findById(processId)
                .orElseThrow(() -> notFoundHandler(processId));
    }

    private NoSuchElementException notFoundHandler(Long processId) {
        var message = messageSource.getMessage(
                "deployment_process.not_found",
                new Object[]{processId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

    private DeploymentProcessHasUser memberIdToEntity(String memberId, DeploymentProcess process)
            throws IllegalArgumentException {
        var processId = process.getId();

        var member = userRepository.findById(memberId).orElseGet(() -> {
            var result = userService.validateUserId(memberId);
            userService.foundOrElseThrow(memberId, result);
            return User.builder().id(memberId).build();
        });

        var id = DeploymentProcessHasUserId.builder()
                .processId(processId)
                .userId(memberId)
                .build();
        return DeploymentProcessHasUser.builder()
                .id(id)
                .process(process)
                .user(member)
                .build();
    }

}
