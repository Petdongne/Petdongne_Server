package org.songeun.petdongne_server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.security.login.OAuth2LoginFailureHandler;
import org.songeun.petdongne_server.security.login.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final AuthenticationFilter authenticationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // URL 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/buildings/{buildingId}/reviews/{reviewId}").authenticated()
                        .requestMatchers("/api/v1/buildings/{buildingId}/reviews").authenticated()
                        .anyRequest().permitAll())

                // oauth 로그인 설정
                .oauth2Login(login -> {
                    login.successHandler(oAuth2LoginSuccessHandler);
                    login.failureHandler(oAuth2LoginFailureHandler);
                })

                .addFilter(corsFilter)
                .addFilter(authenticationFilter)

                // 세션 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // csrf disable
                .csrf(AbstractHttpConfigurer::disable)

                // HTTP Basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)

                // 예외 처리
                .exceptionHandling(exception -> exception
                        // 인증 실패 (401)
                        .authenticationEntryPoint(getAuthenticationEntryPoint())
                        // 인가 실패 (403)
                        .accessDeniedHandler(getAccessDeniedHandler())
                )
        ;

        return http.build();
    }

    private AccessDeniedHandler getAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            GlobalErrorStatus status = GlobalErrorStatus.FORBIDDEN;
            initErrorResponse(response, status);
        };
    }

    private AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            GlobalErrorStatus status = GlobalErrorStatus.UNAUTHORIZED;
            initErrorResponse(response, status);
        };
    }

    private void initErrorResponse(HttpServletResponse response, GlobalErrorStatus status) throws IOException {
        response.setStatus(status.getHttpStatus().value());
        response.setContentType(APPLICATION_JSON_VALUE);
        ApiResponse<Object> body = new ApiResponse<>(
                false, status.getCode(), status.getMessage(), null);
        String bodyStr = objectMapper.writeValueAsString(body);
        response.getWriter().write(bodyStr);
    }
}
