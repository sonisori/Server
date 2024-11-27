package site.sonisori.sonisori.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.response.ErrorResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerConfig implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {
	private final ObjectMapper objectMapper;

	@Override
	public void customize(ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer) {
		httpSecurityExceptionHandlingConfigurer
			.authenticationEntryPoint(this::handleAuthenticationException)
			.accessDeniedHandler(this::handleAccessDeniedException);
	}

	private void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		log.error("Unauthorized request - Method: {}, URI: {}, Error: {}",
			request.getMethod(),
			request.getRequestURI(),
			authException.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		ErrorResponse errorResponse = new ErrorResponse(ErrorMessage.NOT_FOUND_TOKEN.getMessage());

		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(jsonResponse);
	}

	private void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		log.error("Access denied request - Method: {}, URI: {}, Error: {}",
			request.getMethod(),
			request.getRequestURI(),
			accessDeniedException.getMessage());

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");

		ErrorResponse errorResponse = new ErrorResponse(ErrorMessage.ACCESS_DENIED.getMessage()); // 메시지 수정 가능

		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(jsonResponse);
	}
}
