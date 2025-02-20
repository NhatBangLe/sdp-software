package io.github.nhatbangle.sdp.software.repository;

import io.github.nhatbangle.sdp.software.entity.DocumentType;
import io.github.nhatbangle.sdp.software.projection.DocumentTypeInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {

    Page<DocumentTypeInfo> findAllByUser_IdAndNameContainsIgnoreCase(
            @UUID @NotNull String id,
            @NotNull String name,
            @NotNull Pageable pageable
    );

    Optional<DocumentTypeInfo> findInfoById(@UUID @NotNull String id);

}