package io.github.nhatbangle.sdp.software.repository.software;

import io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareVersionInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface SoftwareVersionRepository extends JpaRepository<SoftwareVersion, String> {

    Page<SoftwareVersionInfo> findAllBySoftware_IdAndNameContainsIgnoreCase(
            @UUID @NotNull String id,
            @NotNull String name,
            @NotNull Pageable pageable
    );

    Optional<SoftwareVersionInfo> findInfoById(@UUID @NotNull String versionId);

}