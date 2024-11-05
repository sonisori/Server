package site.sonisori.sonisori.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.cookie.CookieUtil;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final String ACCESS_TOKEN = "access_token";

	private final CookieUtil cookieUtil;
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");

		String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN);
		if (accessToken == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if (jwtUtil.validateAccessToken(accessToken)) {
			Authentication authentication = jwtUtil.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
}
