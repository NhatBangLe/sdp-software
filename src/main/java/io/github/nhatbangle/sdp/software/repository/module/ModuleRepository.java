package io.github.nhatbangle.sdp.software.repository.module;

import io.github.nhatbangle.sdp.software.entity.module.Module;
import io.github.nhatbangle.sdp.software.projection.module.ModuleInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface ModuleRepository extends JpaRepository<Module, String> {

    Page<ModuleInfo> findAllBySoftwareVersion_IdAndNameContainsIgnoreCase(
            @UUID @NotNull String softwareVersionId,
            @NotNull String moduleName,
            @NotNull Pageable pageable
    );

    Optional<ModuleInfo> findInfoById(@UUID @NotNull String id);

}