package io.github.nhatbangle.sdp.software.service;

import io.github.nhatbangle.sdp.software.constant.CacheName;
import io.github.nhatbangle.sdp.software.dto.customer.CustomerCreateRequest;
import io.github.nhatbangle.sdp.software.dto.customer.CustomerResponse;
import io.github.nhatbangle.sdp.software.dto.customer.CustomerUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.entity.Customer;
import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.software.mapper.CustomerMapper;
import io.github.nhatbangle.sdp.software.repository.CustomerRepository;
import io.github.nhatbangle.sdp.software.repository.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Service
@Validated
@CacheConfig(cacheNames = CacheName.CUSTOMER)
@RequiredArgsConstructor
public class CustomerService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final UserRepository userRepository;

    @NotNull
    @Transactional(readOnly = true)
    public PagingWrapper<CustomerResponse> getAll(
            @Nullable String name,
            @Nullable String email,
            int pageNumber,
            int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var page = repository
                .findAllByNameContainsIgnoreCaseAndEmailStartsWithIgnoreCase(
                        Objects.requireNonNullElse(name, ""),
                        Objects.requireNonNullElse(email, ""),
                        pageable
                )
                .map(mapper::toResponse);
        return PagingWrapper.from(page);
    }

    @NotNull
    @Transactional(readOnly = true)
    @Cacheable(key = "#customerId")
    public CustomerResponse getById(
            @UUID @NotNull String customerId
    ) throws NoSuchElementException {
        var customer = repository.findInfoById(customerId)
                .orElseThrow(() -> notFoundHandler(customerId));
        return mapper.toResponse(customer);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#result.id()")
    public CustomerResponse create(
            @UUID @NotNull String userId,
            @NotNull @Valid CustomerCreateRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var user = userRepository.findById(userId).orElseGet(() -> {
            var result = userService.validateUserId(userId);
            userService.foundOrElseThrow(userId, result);
            return User.builder().id(userId).build();
        });

        var customer = repository.save(Customer.builder()
                .name(request.name())
                .email(request.email())
                .creator(user)
                .build());
        return mapper.toResponse(customer);
    }

    @NotNull
    @Transactional
    @CachePut(key = "#customerId")
    public CustomerResponse updateById(
            @UUID @NotNull String customerId,
            @NotNull @Valid CustomerUpdateRequest request
    ) throws NoSuchElementException {
        var customer = findById(customerId);
        customer.setName(request.name());
        customer.setEmail(request.email());

        var savedCustomer = repository.save(customer);
        return mapper.toResponse(savedCustomer);
    }

    @CacheEvict(key = "#customerId")
    public void deleteById(
            @UUID @NotNull String customerId
    ) {
        repository.deleteById(customerId);
    }

    @NotNull
    public Customer findById(@UUID @NotNull String customerId)
            throws NoSuchElementException {
        return repository.findById(customerId)
                .orElseThrow(() -> notFoundHandler(customerId));
    }

    private NoSuchElementException notFoundHandler(String customerId) {
        var message = messageSource.getMessage(
                "customer.not_found",
                new Object[]{customerId},
                Locale.getDefault()
        );
        return new NoSuchElementException(message);
    }

}
