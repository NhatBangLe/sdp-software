package io.github.nhatbangle.sdp.software.config;

import io.github.nhatbangle.sdp.software.mapper.*;
import io.github.nhatbangle.sdp.software.mapper.deployment.DeploymentPhaseMapper;
import io.github.nhatbangle.sdp.software.mapper.deployment.DeploymentPhaseTypeMapper;
import io.github.nhatbangle.sdp.software.mapper.deployment.DeploymentProcessMapper;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleDocumentMapper;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleMapper;
import io.github.nhatbangle.sdp.software.mapper.module.ModuleVersionMapper;
import io.github.nhatbangle.sdp.software.mapper.software.SoftwareDocumentMapper;
import io.github.nhatbangle.sdp.software.mapper.software.SoftwareMapper;
import io.github.nhatbangle.sdp.software.mapper.software.SoftwareVersionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public SoftwareMapper softwareMapper() {
        return new SoftwareMapper();
    }

    @Bean
    public SoftwareVersionMapper softwareVersionMapper() {
        return new SoftwareVersionMapper();
    }

    @Bean
    public SoftwareDocumentMapper softwareDocumentMapper() {
        return new SoftwareDocumentMapper();
    }

    @Bean
    public DocumentTypeMapper documentTypeMapper() {
        return new DocumentTypeMapper();
    }

    @Bean
    public CustomerMapper customerMapper() {
        return new CustomerMapper();
    }

    @Bean
    public ModuleMapper moduleMapper() {
        return new ModuleMapper();
    }

    @Bean
    public ModuleVersionMapper moduleVersionMapper() {
        return new ModuleVersionMapper();
    }

    @Bean
    public ModuleDocumentMapper moduleDocumentMapper() {
        return new ModuleDocumentMapper();
    }

    @Bean
    public DeploymentProcessMapper deploymentProcessMapper() {
        return new DeploymentProcessMapper();
    }

    @Bean
    public DeploymentPhaseMapper deploymentPhaseMapper() {
        return new DeploymentPhaseMapper();
    }

    @Bean
    public DeploymentPhaseTypeMapper deploymentPhaseTypeMapper() {
        return new DeploymentPhaseTypeMapper();
    }

}
