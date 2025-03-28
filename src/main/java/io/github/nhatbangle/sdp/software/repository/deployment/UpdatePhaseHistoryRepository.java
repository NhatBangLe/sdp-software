package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.composite.UpdatePhaseHistoryId;
import io.github.nhatbangle.sdp.software.entity.deployment.UpdatePhaseHistory;
import io.github.nhatbangle.sdp.software.projection.deployment.UpdatePhaseHistoryInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UpdatePhaseHistoryRepository extends JpaRepository<UpdatePhaseHistory, UpdatePhaseHistoryId> {

    Page<UpdatePhaseHistoryInfo> findByPhase_Process_IdAndPhase_Type_NameContainsIgnoreCaseAndDescriptionContainsIgnoreCase(
            @NotNull @Min(0) Long id,
            @NotNull String phaseTypeName,
            @NotNull String description,
            @NotNull Pageable pageable
    );

}