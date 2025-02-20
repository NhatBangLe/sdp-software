package io.github.nhatbangle.sdp.software.repository;

import io.github.nhatbangle.sdp.software.entity.Customer;
import io.github.nhatbangle.sdp.software.projection.customer.CustomerInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Page<CustomerInfo> findAllByNameContainsIgnoreCaseAndEmailStartsWithIgnoreCase(
            @NotNull String name,
            @NotNull String email,
            @NotNull Pageable pageable
    );

    Optional<CustomerInfo> findInfoById(@UUID @NotNull String id);

}