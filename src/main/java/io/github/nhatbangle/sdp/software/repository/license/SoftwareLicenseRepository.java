package io.github.nhatbangle.sdp.software.repository.license;

import io.github.nhatbangle.sdp.software.entity.SoftwareLicense;
import io.github.nhatbangle.sdp.software.projection.SoftwareLicenseInfo;
import io.github.nhatbangle.sdp.software.projection.deployment.SoftwareLicenseDetailInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.stream.Stream;

@Validated
public interface SoftwareLicenseRepository extends JpaRepository<SoftwareLicense, String> {

    Optional<SoftwareLicenseDetailInfo> findDetailInfoById(@NotNull @UUID String id);

    Page<SoftwareLicenseInfo> findAllByProcess_Id(@NotNull @Min(0) Long id, @NotNull Pageable pageable);

    Optional<SoftwareLicenseInfo> findInfoById(@UUID @NotNull String id);

    @Query("""
            select s from SoftwareLicense s
            where s.isExpireAlertDone = :isAlertDone
                and s.startTime <= current_time
                and current_time <= s.endTime
            """)
    Stream<SoftwareLicense> findAllPotentiallyExpiredLicenses(
            @Param("isAlertDone") boolean isExpireAlertDone,
            @NotNull Sort sort
    );

}