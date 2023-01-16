package ru.otus.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String initSchemaFileName;
    private String updateSchemaFileName;

    public String getInitSchemaFileName() {
        return initSchemaFileName;
    }

    public AppConfig setInitSchemaFileName(String initSchemaFileName) {
        this.initSchemaFileName = initSchemaFileName;
        return this;
    }

    public String getUpdateSchemaFileName() {
        return updateSchemaFileName;
    }

    public AppConfig setUpdateSchemaFileName(String updateSchemaFileName) {
        this.updateSchemaFileName = updateSchemaFileName;
        return this;
    }
}
