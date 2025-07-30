package org.songeun.petdongne_server.global.notification;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Valid
@ConfigurationProperties("app.notification")
public class NotificationProperties {

    @NotBlank
    private final String discordWebhookUrl;

    public NotificationProperties(String discordWebhookUrl) {
        this.discordWebhookUrl = discordWebhookUrl;
    }

}
