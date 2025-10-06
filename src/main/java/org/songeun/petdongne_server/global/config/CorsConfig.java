package org.songeun.petdongne_server.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

/*    @Value("${spring.security.cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Value("${spring.security.cors.allowed-methods}")
    private String[] corsAllowedMethods;

    @Value("${spring.security.cors.allowed-headers}")
    private String[] corsAllowedHeaders;

    @Value("${spring.security.cors.path-pattern}")
    private String corsPathPattern;

    @Value("${spring.security.auth.header}")
    private String authHeader;*/

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.addExposedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
