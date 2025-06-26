package org.songeun.petdongne_server.address.infrastructure.elasticsearch.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Valid
@ConfigurationProperties("app.elasticsearch")
public class ElasticsearchClientProperties {

    @NotBlank
    private final String hostAndPort;

    @Positive(message = "연결 타임아웃은 양수여야 합니다.")
    private final Integer connectionTimeout;

    @Positive(message = "소켓 타임아웃은 양수여야 합니다.")
    private final Integer socketTimeout;

    public ElasticsearchClientProperties(String hostAndPort, Integer connectionTimeout, Integer socketTimeout) {
        this.hostAndPort = hostAndPort;
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

}
