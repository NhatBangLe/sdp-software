package io.github.nhatbangle.sdp.software.repository.software;

import io.github.nhatbangle.sdp.software.entity.software.Software;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface SoftwareRepository extends JpaRepository<Software, String> {

    Page<SoftwareInfo> findAllByUser_IdAndNameContainsIgnoreCase(
            @UUID @NotNull String id,
            @NotNull String name,
            @NotNull Pageable pageable
    );

    Optional<SoftwareInfo> findInfoById(@UUID @NotNull String id);
}