package site.sonisori.sonisori.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.dto.CustomOAuth2User;
import site.sonisori.sonisori.jwt.JwtUtil;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws
		IOException, ServletException {

		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();

		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String userId = customUserDetails.getName();

		String token = jwtUtil.createJwt(username, role, userId, 60 * 60 * 60L);

		response.addHeader("Set-Cookie", createCookie("Authorization", token).toString());
		response.sendRedirect("<http://localhost:8080/>");
	}

	private ResponseCookie createCookie(String key, String value) {
		return ResponseCookie.from(key, value)
			.maxAge(60 * 60 * 60)
			.path("/")
			.httpOnly(false)
			.domain("localhost")
			.build();
	}

}

