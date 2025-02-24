package io.github.nhatbangle.sdp.software.service;

import io.github.nhatbangle.sdp.software.constant.MailTemplateType;
import io.github.nhatbangle.sdp.software.dto.mail.MailTemplateCreateRequest;
import io.github.nhatbangle.sdp.software.dto.mail.MailTemplateResponse;
import io.github.nhatbangle.sdp.software.dto.mail.MailTemplateUpdateRequest;
import io.github.nhatbangle.sdp.software.entity.MailTemplate;
import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.MailTemplateMapper;
import io.github.nhatbangle.sdp.software.repository.MailTemplateRepository;
import io.github.nhatbangle.sdp.software.repository.UserRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Validated
@CacheConfig(cacheNames = "sdp_software-mail_template")
@RequiredArgsConstructor
public class MailTemplateService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MailTemplateMapper mapper;
    private final MailTemplateRepository repository;

    public MailTemplateResponse getByUserIdAndType(
            @NotNull @UUID String userId,
            @NotNull MailTemplateType type
    ) throws NoSuchElementException {
        var template = findByUserIdAndType(userId, type);
        return mapper.toResponse(template);
    }

    @Cacheable(key = "#templateId")
    public MailTemplateResponse getById(
            @UUID @NotNull String templateId
    ) throws NoSuchElementException {
        var customer = repository.findInfoById(templateId)
                .orElseThrow(() -> notFoundHandler(templateId));
        return mapper.toResponse(customer);
    }

    @Transactional
    @CachePut(key = "#result.id()")
    public MailTemplateResponse create(
            @UUID @NotNull String userId,
            @NotNull @Valid MailTemplateCreateRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var charset = StandardCharsets.UTF_8;
        var templateType = request.type();
        var content = request.content().getBytes(charset);

        var templateOptional = repository.findByUser_IdAndType(userId, templateType);
        if (templateOptional.isPresent()) {
            var template = templateOptional.get();
            template.setContent(content);

            var savedTemplate = repository.save(template);
            return mapper.toResponse(savedTemplate);
        }

        var user = userRepository.findById(userId).orElseGet(() -> {
            var result = userService.validateUserId(userId);
            userService.foundOrElseThrow(userId, result);
            return User.builder().id(userId).build();
        });

        var template = repository.save(MailTemplate.builder()
                .content(request.content().getBytes(charset))
                .type(request.type())
                .user(user)
                .build());
        return mapper.toResponse(template);
    }

    @Transactional
    @CachePut(key = "#templateId")
    public MailTemplateResponse updateById(
            @UUID @NotNull String templateId,
            @NotNull @Valid MailTemplateUpdateRequest request
    ) throws NoSuchElementException {
        var template = findById(templateId);
        template.setContent(request.content().getBytes(StandardCharsets.UTF_8));
        template.setType(request.type());

        var savedTemplate = repository.save(template);
        return mapper.toResponse(savedTemplate);
    }

    @CacheEvict(key = "#templateId")
    public void deleteById(
            @UUID @NotNull String templateId
    ) throws NoSuchElementException {
        var customer = findById(templateId);
        repository.delete(customer);
    }

    @NotNull
    public MailTemplate findById(@UUID @NotNull String templateId)
            throws NoSuchElementException {
        return repository.findById(templateId)
                .orElseThrow(() -> notFoundHandler(templateId));
    }

    @NotNull
    public MailTemplate findByUserIdAndType(
            @NotNull @UUID String userId,
            @NotNull MailTemplateType type
    ) throws NoSuchElementException {
        return repository.findByUser_IdAndType(userId, type).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "mail_template.not_found_by_userId_and_type",
                    new Object[]{type.name(), userId},
                    Locale.getDefault()
            );
            return new NoSuchElementException(message);
        });
    }

    private NoSuchElementException notFoundHandler(String templateId) {
        var message = messageSource.getMessage(
                "mail_template.not_found",
                new Object[]{templateId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
