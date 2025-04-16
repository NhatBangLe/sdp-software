package io.github.nhatbangle.sdp.software.service.deployment;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.constant.MailTemplatePlaceholder;
import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import io.github.nhatbangle.sdp.software.dto.*;
import io.github.nhatbangle.sdp.software.dto.notification.MailSendPayload;
import io.github.nhatbangle.sdp.software.dto.notification.NotificationSendPayload;
import io.github.nhatbangle.sdp.software.entity.SoftwareLicense;
import io.github.nhatbangle.sdp.software.mapper.deployment.SoftwareLicenseMapper;
import io.github.nhatbangle.sdp.software.repository.license.SoftwareLicenseRepository;
import io.github.nhatbangle.sdp.software.service.MailTemplateService;
import io.github.nhatbangle.sdp.software.service.NotificationService;
import io.github.nhatbangle.sdp.software.service.RedisCacheService;
import io.github.nhatbangle.sdp.software.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheName.LICENSE)
public class SoftwareLicenseService {

    private final MessageSource messageSource;
    private final SoftwareLicenseRepository repository;
    private final DeploymentProcessService processService;
    private final SoftwareLicenseMapper mapper;
    private final UserService userService;
    private final MailTemplateService mailTemplateService;
    private final NotificationService notificationService;
    private final RedisCacheService redisCacheService;

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
    @Transactional(readOnly = true)
    @Cacheable(key = "#licenseId", cacheNames = CacheName.LICENSE_DETAIL)
    public SoftwareLicenseDetailResponse getDetailById(
            @UUID @NotNull String licenseId
    ) throws NoSuchElementException {
        var license = repository.findDetailInfoById(licenseId)
                .orElseThrow(() -> notFoundHandler(licenseId));
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

        sendAlertMail(license.getId(), MailTemplateType.NEW_LICENSE_CREATED_ALERT);

        return mapper.toResponse(license);
    }

    @Transactional(readOnly = true)
    public boolean sendAlertMail(@UUID @NotNull String licenseId,
                                         @NotNull MailTemplateType type) {
        var license = repository.findDetailInfoById(licenseId).orElseThrow(() -> notFoundHandler(licenseId));
        var creator = license.getCreator();
        var process = license.getProcess();
        var customer = process.getCustomer();
        var softVer = process.getSoftwareVersion();
        var software = softVer.getSoftware();

        var charset = MailTemplateService.DEFAULT_CHARSET;
        var template = mailTemplateService.findByUserIdAndType(creator.getId(), type);
        var content = new String(template.getContent(), charset)
                .replace(MailTemplatePlaceholder.CUSTOMER_NAME.getVarName(), customer.getName())
                .replace(MailTemplatePlaceholder.DEPLOYMENT_PROCESS_ID.getVarName(), process.getId().toString())
                .replace(MailTemplatePlaceholder.SOFTWARE_NAME.getVarName(), software.getName())
                .replace(MailTemplatePlaceholder.SOFTWARE_VERSION.getVarName(), softVer.getName())
                .replace(MailTemplatePlaceholder.LICENSE_ID.getVarName(), license.getId())
                .replace(MailTemplatePlaceholder.LICENSE_START_TIME.getVarName(), license.getStartTime().toString())
                .replace(MailTemplatePlaceholder.LICENSE_END_TIME.getVarName(), license.getEndTime().toString());
        return notificationService.sendMail(new MailSendPayload(
                template.getSubject(),
                content.getBytes(charset),
                charset.name(),
                customer.getEmail()
        ));
    }

    @NotNull
    @Transactional
    @CachePut(key = "#licenseId", cacheNames = {CacheName.LICENSE, CacheName.LICENSE_DETAIL})
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

    @CacheEvict(key = "#licenseId", cacheNames = {CacheName.LICENSE, CacheName.LICENSE_DETAIL})
    public void deleteById(
            @UUID @NotNull String licenseId
    ) throws NoSuchElementException {
        repository.deleteById(licenseId);
    }

    @NotNull
    public SoftwareLicense findById(@UUID @NotNull String phaseId)
            throws NoSuchElementException {
        return repository.findById(phaseId)
                .orElseThrow(() -> notFoundHandler(phaseId));
    }

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<SoftwareLicenseResponse> getAllPotentiallyExpiredLicense(
            Boolean isExpireAlertDone,
            @Min(0) int pageNumber,
            @Min(1) @Max(50) int pageSize
    ) {
        var rawResult = findAllPotentiallyExpiredLicenses(isExpireAlertDone).toList();
        var size = rawResult.size();
        var totalPages = Math.ceilDiv(size, pageSize);
        var startIndex = Math.max(0, pageNumber * pageSize);
        var result = rawResult.subList(Math.min(startIndex, size), Math.min(startIndex + pageSize, size))
                .stream().map(mapper::toResponse).toList();

        var wrapper = new PagingWrapper<SoftwareLicenseResponse>();
        wrapper.setTotalPages(totalPages);
        wrapper.setNumber(pageNumber);
        wrapper.setSize(pageSize);
        wrapper.setTotalElements((long) size);
        wrapper.setFirst(pageNumber == 0);
        wrapper.setLast(pageNumber == totalPages - 1);
        wrapper.setNumberOfElements(result.size());
        wrapper.setContent(result);

        return wrapper;
    }

    @NotNull
    @Transactional(readOnly = true)
    protected Stream<SoftwareLicense> findAllPotentiallyExpiredLicenses(Boolean isExpireAlertDone) {
        var currentTime = Instant.now();
        return repository.findLicensesByIsAlertDoneAndMiddleTime(
                isExpireAlertDone,
                currentTime,
                Sort.by("startTime").ascending()
        ).filter(license -> {
            var interval = license.getExpireAlertIntervalDay();
            var endTime = license.getEndTime();
            var daysBetween = ChronoUnit.DAYS.between(currentTime, endTime);

            return daysBetween >= 0 && daysBetween <= interval;
        });
    }

    @Transactional
    public void sendExpirationAlert() throws NoSuchElementException {
        var licenseStream = findAllPotentiallyExpiredLicenses(false);
        var filtered = licenseStream.filter(license -> {
            var process = license.getProcess();
            var processCreator = process.getCreator();
            var creatorId = process.getCreator().getId();

            try {
                // send alert mail to customer
                var sendingMailResult = sendAlertMail(license.getId(),
                        MailTemplateType.SOFTWARE_EXPIRE_ALERT);
                if (!sendingMailResult) return false;

                // send notification to admin
                notificationService.sendNotification(new NotificationSendPayload(
                        messageSource.getMessage("expired_license.notify.title", null, Locale.getDefault()),
                        messageSource.getMessage("expired_license.notify.description",
                                new Object[]{license.getId()}, Locale.getDefault()),
                        List.of(processCreator.getId())
                ));

                license.setIsExpireAlertDone(true);
                redisCacheService.invalidateKey(CacheName.LICENSE_DETAIL, license.getId());
                return true;
            } catch (NoSuchElementException e) {
                log.warn("""
                        Could not send expiration alert mail.
                        Because the user with id {} doesn't have expiration template.
                        """, creatorId);
                log.debug(e.getMessage(), e);
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
