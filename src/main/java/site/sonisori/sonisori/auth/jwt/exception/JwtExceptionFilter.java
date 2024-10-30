package site.sonisori.sonisori.auth.jwt.exception;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.response.ErrorResponse;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");

		try {
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			setErrorResponse(response, ErrorMessage.EXPIRED_TOKEN.getMessage());
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			setErrorResponse(response, ErrorMessage.INVALID_TOKEN.getMessage());
		} catch (JwtException e) {
			setErrorResponse(response, ErrorMessage.NOT_FOUND_TOKEN.getMessage());
		}
	}

	private void setErrorResponse(HttpServletResponse response, String message)
		throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		ErrorResponse errorResponse = new ErrorResponse(message);

		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(jsonResponse);
	}
}
