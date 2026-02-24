package org.songeun.petdongne_server.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class OpaqueTokenAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        GlobalErrorStatus status = GlobalErrorStatus.UNAUTHORIZED;
        response.setStatus(status.getHttpStatus().value());
        response.setContentType(APPLICATION_JSON_VALUE);
        ApiResponse<Object> body = new ApiResponse<>(
                false, status.getCode(), status.getMessage(), null);
        String bodyStr = objectMapper.writeValueAsString(body);
        response.getWriter().write(bodyStr);
    }
}
