package io.github.nhatbangle.sdp.software.controller;

import io.github.nhatbangle.sdp.software.dto.customer.CustomerCreateRequest;
import io.github.nhatbangle.sdp.software.dto.customer.CustomerResponse;
import io.github.nhatbangle.sdp.software.dto.customer.CustomerUpdateRequest;
import io.github.nhatbangle.sdp.software.dto.PagingWrapper;
import io.github.nhatbangle.sdp.software.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@Tag(name = "Customer")
@RequiredArgsConstructor
@RequestMapping("/api/v1/software/customer")
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<CustomerResponse> getAll(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        return service.getAll(
                email,
                name,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getById(
            @PathVariable @UUID String customerId
    ) {
        return service.getById(customerId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(
            @PathVariable @UUID String userId,
            @RequestBody @Valid CustomerCreateRequest request
    ) {
        return service.create(userId, request);
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @PathVariable @UUID String customerId,
            @RequestBody @Valid CustomerUpdateRequest request
    ) {
        service.updateById(customerId, request);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @UUID String customerId
    ) {
        service.deleteById(customerId);
    }

}
