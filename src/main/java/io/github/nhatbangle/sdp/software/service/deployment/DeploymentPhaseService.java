package io.github.nhatbangle.sdp.software.service.deployment;

import io.github.nhatbangle.sdp.software.dto.deployment.*;
import io.github.nhatbangle.sdp.software.dto.AttachmentUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.Attachment;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentPhaseHasAttachmentId;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentPhaseHasUserId;
import io.github.nhatbangle.sdp.software.entity.composite.UpdatePhaseHistoryId;
import io.github.nhatbangle.sdp.software.entity.deployment.*;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.deployment.DeploymentPhaseMapper;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentPhaseHasAttachmentRepository;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentPhaseRepository;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentProcessHasUserRepository;
import io.github.nhatbangle.sdp.software.repository.deployment.DeploymentPhaseHasUserRepository;
import io.github.nhatbangle.sdp.software.repository.deployment.UpdatePhaseHistoryRepository;
import io.github.nhatbangle.sdp.software.service.AttachmentService;
import io.github.nhatbangle.sdp.software.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "sdp_software-deployment_phase")
public class DeploymentPhaseService {

    private final DeploymentPhaseHasAttachmentRepository phaseAtmRepository;
    private final MessageSource messageSource;
    private final DeploymentPhaseRepository repository;
    private final DeploymentPhaseMapper mapper;
    private final DeploymentProcessService deploymentProcessService;
    private final DeploymentPhaseTypeService deploymentPhaseTypeService;
    private final UserService userService;
    private final UpdatePhaseHistoryRepository updatePhaseHistoryRepository;
    private final DeploymentPhaseHasUserRepository deploymentPhaseHasUserRepository;
    private final DeploymentProcessHasUserRepository processHasUserRepository;
    private final AttachmentService atmService;

    @NotNull
    @Transactional(readOnly = true)
    public List<DeploymentPhaseResponse> getAllByProcessId(
            @Min(0) @NotNull Long processId
    ) {
        var phases = repository.findAllByProcess_Id(processId);
        return phases.stream().map(mapper::toResponse).toList();
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#phaseId")
    public DeploymentPhaseResponse getById(
            @UUID @NotNull String phaseId
    ) throws NoSuchElementException {
        var process = repository.findInfoById(phaseId)
                .orElseThrow(() -> notFoundHandler(phaseId));
        return mapper.toResponse(process);
    }

    @NotNull
    @Transactional(readOnly = true)
    public List<String> getAllAttachments(@NotNull @UUID String phaseId) {
        return phaseAtmRepository.findAllById_PhaseId(
                        phaseId, Sort.by("createdAt").ascending())
                .map(atm -> atm.getId().getAttachmentId())
                .toList();
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "sdp_software-deployment_phase-member", key = "#phaseId")
    public List<String> getAllMembers(
            @UUID @NotNull String phaseId
    ) throws NoSuchElementException {
        var members = deploymentPhaseHasUserRepository
                .findAllById_PhaseId(phaseId, Sort.by("createdAt").ascending());
        return members.map(member -> member.getId().getUserId()).toList();
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public DeploymentPhaseResponse create(
            @Min(0) @NotNull Long processId,
            @NotNull @Valid DeploymentPhaseCreateRequest request
    ) throws IllegalArgumentException {
        var process = deploymentProcessService.findById(processId);
        var type = deploymentPhaseTypeService.findById(request.phaseTypeId());
        var plannedStartDate = request.plannedStartDate();
        var plannedEndDate = request.plannedEndDate();
        validateStartEndDates(plannedStartDate, plannedEndDate);

        var phase = repository.save(DeploymentPhase.builder()
                .numOrder(request.numOrder())
                .description(request.description())
                .plannedStartDate(plannedStartDate)
                .plannedEndDate(plannedEndDate)
                .type(type)
                .process(process)
                .build());
        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            var phaseId = phase.getId();
            var attachments = attachmentIds.stream().map(atmId -> {
                var id = DeploymentPhaseHasAttachmentId.builder()
                        .attachmentId(atmId)
                        .phaseId(phaseId)
                        .build();
                return DeploymentPhaseHasAttachment.builder()
                        .id(id)
                        .attachment(Attachment.builder().id(atmId).build())
                        .phase(phase)
                        .build();
            }).toList();
            phaseAtmRepository.saveAll(attachments);
        }

        return mapper.toResponse(phase);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#phaseId")
    public DeploymentPhaseResponse updateById(
            @UUID @NotNull String phaseId,
            @NotNull @Valid DeploymentPhaseUpdateRequest request
    ) throws NoSuchElementException, IllegalArgumentException {
        var plannedStartDate = request.plannedStartDate();
        var plannedEndDate = request.plannedEndDate();
        validateStartEndDates(plannedStartDate, plannedEndDate);

        var phase = findById(phaseId);
        phase.setNumOrder(request.numOrder());
        phase.setDescription(request.description());
        phase.setPlannedStartDate(plannedStartDate);
        phase.setPlannedEndDate(plannedEndDate);

        var savedPhase = repository.save(phase);
        return mapper.toResponse(savedPhase);
    }

    @Transactional
    public void updateAttachment(
            @UUID @NotNull String phaseId,
            @NotNull @Valid AttachmentUpdateRequest request
    ) throws NoSuchElementException, IllegalArgumentException, ServiceUnavailableException {
        var phase = findById(phaseId);
        var attachmentId = request.attachmentId();
        var result = atmService.isFileExist(attachmentId);
        atmService.foundOrElseThrow(attachmentId, result);

        var id = DeploymentPhaseHasAttachmentId.builder()
                .attachmentId(request.attachmentId())
                .phaseId(phaseId)
                .build();
        switch (request.operator()) {
            case ADD -> {
                if (phaseAtmRepository.existsById_PhaseIdAndId_AttachmentId(phaseId, attachmentId)) {
                    var message = messageSource.getMessage(
                            "deployment_phase.attachment_already_exists",
                            new Object[]{attachmentId, phaseId},
                            Locale.getDefault()
                    );
                    throw new IllegalArgumentException(message);
                }
                var attachment = DeploymentPhaseHasAttachment.builder()
                        .id(id)
                        .attachment(Attachment.builder().id(request.attachmentId()).build())
                        .phase(phase)
                        .build();
                phaseAtmRepository.save(attachment);
            }
            case REMOVE -> phaseAtmRepository.deleteById(id);
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "sdp_software-deployment_phase-member", key = "#phaseId")
    public void updateMember(
            @UUID @NotNull String phaseId,
            @NotNull @Valid DeploymentPhaseMemberUpdateRequest request
    ) throws NoSuchElementException, IllegalArgumentException {
        var phase = findById(phaseId);
        switch (request.operator()) {
            case ADD -> {
                var member = memberIdToEntity(request.memberId(), phase);
                deploymentPhaseHasUserRepository.save(member);
            }
            case REMOVE -> deploymentPhaseHasUserRepository
                    .deleteById_PhaseIdAndId_UserId(phaseId, request.memberId());
        }
        repository.save(phase);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#phaseId")
    public DeploymentPhaseResponse updateActualDates(
            @UUID @NotNull String phaseId,
            @NotNull @Valid DeploymentPhaseUpdateActualDatesRequest request
    ) throws NoSuchElementException, IllegalArgumentException {
        var startDate = request.actualStartDate();
        var endDate = request.actualEndDate();
        validateStartEndDates(startDate, endDate);

        var userId = request.updatedByUserId();
        var user = userService.getById(userId);

        var isPhaseDone = endDate != null;
        var phase = findById(phaseId);
        phase.setActualStartDate(startDate);
        phase.setActualEndDate(endDate);
        phase.setUserLastUpdate(user);
        phase.setIsDone(isPhaseDone);

        var id = UpdatePhaseHistoryId.builder()
                .phaseId(phaseId)
                .userId(userId)
                .build();
        var history = UpdatePhaseHistory.builder()
                .id(id)
                .phase(phase)
                .user(user)
                .isDone(isPhaseDone)
                .description(request.description())
                .build();
        updatePhaseHistoryRepository.save(history);

        var savedPhase = repository.save(phase);
        return mapper.toResponse(savedPhase);
    }

    @CacheEvict(key = "#phaseId")
    public void deleteById(
            @UUID @NotNull String phaseId
    ) throws NoSuchElementException {
        var phase = findById(phaseId);
        repository.delete(phase);
    }

    @NotNull
    public DeploymentPhase findById(@UUID @NotNull String phaseId)
            throws NoSuchElementException {
        return repository.findById(phaseId)
                .orElseThrow(() -> notFoundHandler(phaseId));
    }

    private NoSuchElementException notFoundHandler(String phaseId) {
        var message = messageSource.getMessage(
                "deployment_phase.not_found",
                new Object[]{phaseId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

    private void validateStartEndDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null) {
            if (endDate != null && startDate.isAfter(endDate)) {
                var message = messageSource.getMessage(
                        "deployment_phase.start_date_after_end_date",
                        new Object[]{startDate, endDate},
                        Locale.getDefault()
                );
                throw new IllegalArgumentException(message);
            }
        } else if (endDate != null) {
            var message = messageSource.getMessage(
                    "deployment_phase.phase_has_not_start_date",
                    null,
                    Locale.getDefault()
            );
            throw new IllegalArgumentException(message);
        }
    }

    private DeploymentPhaseHasUser memberIdToEntity(String memberId, DeploymentPhase phase)
            throws IllegalArgumentException {
        var phaseId = phase.getId();
        var processId = phase.getProcess().getId();
        var user = processHasUserRepository
                .findById_ProcessIdAndId_UserId(processId, memberId)
                .orElseThrow(() -> {
                    var message = messageSource.getMessage(
                            "deployment_phase.member_not_found",
                            new Object[]{memberId, processId},
                            Locale.getDefault()
                    );
                    return new IllegalArgumentException(message);
                });

        var id = DeploymentPhaseHasUserId.builder()
                .phaseId(phaseId)
                .userId(memberId)
                .build();
        return DeploymentPhaseHasUser.builder()
                .id(id)
                .phase(phase)
                .user(user.getUser())
                .build();
    }

}
