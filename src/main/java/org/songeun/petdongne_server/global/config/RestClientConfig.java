package org.songeun.petdongne_server.global.config;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.notification.NotificationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final NotificationProperties notificationProperties;

    @Bean
    public RestClient discordClient() {
        return RestClient.builder()
                .baseUrl(notificationProperties.getDiscordWebhookUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}
