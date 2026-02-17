package org.songeun.petdongne_server.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Valid
@ConfigurationProperties("app.frontend")
public class FrontendUrlProperties {

    @NotBlank
    private final String url;

    public FrontendUrlProperties(String url) {
        this.url = url;
    }
}
