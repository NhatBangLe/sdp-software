package io.github.nhatbangle.sdp.software.repository.module;

import io.github.nhatbangle.sdp.software.entity.module.ModuleDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleDocumentRepository extends JpaRepository<ModuleDocument, String> {
}