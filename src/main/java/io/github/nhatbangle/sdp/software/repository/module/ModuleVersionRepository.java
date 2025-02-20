package io.github.nhatbangle.sdp.software.repository.module;

import io.github.nhatbangle.sdp.software.entity.module.ModuleVersion;
import io.github.nhatbangle.sdp.software.projection.module.ModuleVersionInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface ModuleVersionRepository extends JpaRepository<ModuleVersion, String> {

    Page<ModuleVersionInfo> findAllByModule_IdAndNameContainsIgnoreCase(
            @UUID @NotNull String id,
            @NotNull String name,
            @NotNull Pageable pageable
    );

    Optional<ModuleVersionInfo> findInfoById(@UUID @NotNull String id);

}