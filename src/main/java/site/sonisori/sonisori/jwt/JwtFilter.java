package site.sonisori.sonisori.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.dto.CustomOAuth2User;
import site.sonisori.sonisori.dto.UserDto;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorization = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {

			if (cookie.getName().equals("Authorization")) {
				authorization = cookie.getValue();
			}
		}

		if (authorization == null) {

			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization;

		if (jwtUtil.isExpired(token)) {

			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);
		String name = jwtUtil.getUserId(token);

		UserDto userDto = new UserDto(role, name, username);

		CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
			customOAuth2User.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}
