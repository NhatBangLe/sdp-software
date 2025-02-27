package io.github.nhatbangle.sdp.software.mapper;

import io.github.nhatbangle.sdp.software.dto.customer.CustomerResponse;
import io.github.nhatbangle.sdp.software.entity.Customer;
import io.github.nhatbangle.sdp.software.projection.CustomerInfo;

public class CustomerMapper {

    public CustomerResponse toResponse(CustomerInfo entity) {
        var updatedAt = entity.getUpdatedAt();
        return new CustomerResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public CustomerResponse toResponse(Customer entity) {
        var updatedAt = entity.getUpdatedAt();
        return new CustomerResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCreatedAt().toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

}
