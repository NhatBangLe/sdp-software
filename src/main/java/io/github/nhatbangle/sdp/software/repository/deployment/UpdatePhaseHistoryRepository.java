package io.github.nhatbangle.sdp.software.repository.deployment;

import io.github.nhatbangle.sdp.software.entity.composite.UpdatePhaseHistoryId;
import io.github.nhatbangle.sdp.software.entity.deployment.UpdatePhaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdatePhaseHistoryRepository extends JpaRepository<UpdatePhaseHistory, UpdatePhaseHistoryId> {
}