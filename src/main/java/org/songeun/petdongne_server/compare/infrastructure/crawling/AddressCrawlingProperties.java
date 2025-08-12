package org.songeun.petdongne_server.compare.infrastructure.crawling;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Valid
@ConfigurationProperties("app.crawling.address")
public class AddressCrawlingProperties {

    @NotBlank
    private final String postListBaseUrl;

    @NotBlank
    private final String postBaseUrl;

    @NotBlank
    private final String userAgent;

    @NotBlank
    private final String outputFileSavePath;

    public AddressCrawlingProperties(String postListBaseUrl, String postBaseUrl, String userAgent, String outputFileSavePath) {
        this.postListBaseUrl = postListBaseUrl;
        this.postBaseUrl = postBaseUrl;
        this.userAgent = userAgent;
        this.outputFileSavePath = outputFileSavePath;
    }

}
