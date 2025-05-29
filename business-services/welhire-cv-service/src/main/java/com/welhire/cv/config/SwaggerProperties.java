package com.welhire.cv.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private String title;
    private String description;
    private String version;
    private Contact contact = new Contact();
    private License license = new License();

    @Data
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

    @Data
    public static class License {
        private String name;
        private String url;
    }
}
