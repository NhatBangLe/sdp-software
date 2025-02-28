package io.github.nhatbangle.sdp.software.service.deployment;

import io.github.nhatbangle.sdp.software.constant.MailTemplatePlaceholder;
import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseCreateRequest;
import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseResponse;
import io.github.nhatbangle.sdp.software.dto.SoftwareLicenseUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.mail.MailSendPayload;
import io.github.nhatbangle.sdp.software.entity.SoftwareLicense;
import io.github.nhatbangle.sdp.software.mapper.deployment.SoftwareLicenseMapper;
import io.github.nhatbangle.sdp.software.repository.license.SoftwareLicenseRepository;
import io.github.nhatbangle.sdp.software.service.MailTemplateService;
import io.github.nhatbangle.sdp.software.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UUID;
import org.springframework.amqp.AmqpException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "sdp_software-software_license")
public class SoftwareLicenseService {

    private final MessageSource messageSource;
    private final SoftwareLicenseRepository repository;
    private final DeploymentProcessService processService;
    private final SoftwareLicenseMapper mapper;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;
    private final MailTemplateService mailTemplateService;

    @Value("${app.mail-box-queue}")
    private String mailBoxQueue;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<SoftwareLicenseResponse> getAllByProcessId(
            @Min(0) @NotNull Long processId,
            @Min(0) int pageNumber,
            @Min(1) @Max(50) int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository.findAllByProcess_Id(processId, pageable).map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#phaseId")
    public SoftwareLicenseResponse getById(
            @UUID @NotNull String phaseId
    ) throws NoSuchElementException {
        var license = repository.findInfoById(phaseId)
                .orElseThrow(() -> notFoundHandler(phaseId));
        return mapper.toResponse(license);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public SoftwareLicenseResponse create(
            @NotNull @UUID String userId,
            @NotNull @Valid SoftwareLicenseCreateRequest request
    ) throws IllegalArgumentException {
        var process = processService.findById(request.processId());
        var user = userService.getById(userId);

        var license = repository.save(SoftwareLicense.builder()
                .description(request.description())
                .startTime(Instant.ofEpochMilli(request.startTimeMs()))
                .endTime(Instant.ofEpochMilli(request.endTimeMs()))
                .expireAlertIntervalDay(request.expireAlertIntervalDay())
                .process(process)
                .creator(user)
                .build());
        return mapper.toResponse(license);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#licenseId")
    public SoftwareLicenseResponse updateById(
            @UUID @NotNull String licenseId,
            @NotNull @Valid SoftwareLicenseUpdateRequest request
    ) throws NoSuchElementException, IllegalArgumentException {
        var license = findById(licenseId);
        license.setDescription(request.description());
        license.setExpireAlertIntervalDay(request.expireAlertIntervalDay());

        var savedLicense = repository.save(license);
        return mapper.toResponse(savedLicense);
    }

    @CacheEvict(key = "#licenseId")
    public void deleteById(
            @UUID @NotNull String licenseId
    ) throws NoSuchElementException {
        var phase = findById(licenseId);
        repository.delete(phase);
    }

    @NotNull
    public SoftwareLicense findById(@UUID @NotNull String phaseId)
            throws NoSuchElementException {
        return repository.findById(phaseId)
                .orElseThrow(() -> notFoundHandler(phaseId));
    }

    @NotNull
    public Stream<SoftwareLicense> findAllAlmostExpiredLicense() {
        return repository.findAllPotentiallyExpiredLicenses(
                false,
                Sort.by("startTime").ascending()
        ).filter(license -> {
            var interval = license.getExpireAlertIntervalDay();
            var current = Instant.now();
            var endTime = license.getEndTime();

            return current.plus(interval, ChronoUnit.DAYS).compareTo(endTime) >= 0;
        });
    }

    @Transactional
    public void sendExpirationAlertMail(
            @NotNull Stream<SoftwareLicense> licenseStream
    ) throws NoSuchElementException {
        var filtered = licenseStream.filter(license -> {
            var charset = MailTemplateService.DEFAULT_CHARSET;
            var process = license.getProcess();
            var creatorId = process.getCreator().getId();
            try {
                var template = mailTemplateService.findByUserIdAndType(creatorId, MailTemplateType.SOFTWARE_EXPIRE_ALERT);
                var customer = process.getCustomer();
                var softwareVersion = process.getSoftwareVersion();
                var software = softwareVersion.getSoftware();
                var content = new String(template.getContent(), charset)
                        .replace(MailTemplatePlaceholder.CUSTOMER_NAME.name(), customer.getName())
                        .replace(MailTemplatePlaceholder.DEPLOYMENT_PROCESS_ID.name(), process.getId().toString())
                        .replace(MailTemplatePlaceholder.SOFTWARE_NAME.name(), software.getName())
                        .replace(MailTemplatePlaceholder.SOFTWARE_VERSION.name(), softwareVersion.getName())
                        .replace(MailTemplatePlaceholder.LICENSE_ID.name(), license.getId())
                        .replace(MailTemplatePlaceholder.LICENSE_START_TIME.name(), license.getStartTime().toString())
                        .replace(MailTemplatePlaceholder.LICENSE_END_TIME.name(), license.getEndTime().toString());

                var payload = new MailSendPayload(
                        template.getSubject(),
                        content.getBytes(charset),
                        charset.name(),
                        customer.getEmail()
                );
                rabbitTemplate.convertAndSend(mailBoxQueue, payload);

                license.setIsExpireAlertDone(true);
                return true;
            } catch (NoSuchElementException e) {
                log.warn("""
                        Could not send expiration alert mail.
                        Because user with id {} doesn't have expiration template.
                        """, creatorId);
                log.debug(e.getMessage(), e);

                return false;
            } catch (AmqpException e) {
                log.warn("""
                        Could not send expiration alert mail.
                        Because the AMQP exception has been thrown.
                        """);
                log.error(e.getMessage(), e);

                return false;
            }
        });
        repository.saveAll(filtered.toList());
    }

    private NoSuchElementException notFoundHandler(String phaseId) {
        var message = messageSource.getMessage(
                "software_license.not_found",
                new Object[]{phaseId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }
}
