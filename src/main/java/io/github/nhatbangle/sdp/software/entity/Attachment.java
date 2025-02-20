package io.github.nhatbangle.sdp.software.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "attachment")
public class Attachment {
    @Id
    @UUID
    @NotNull
    @Column(name = "id", nullable = false, length = 36)
    private String id;
}