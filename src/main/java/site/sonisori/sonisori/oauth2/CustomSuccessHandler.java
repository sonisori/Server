package site.sonisori.sonisori.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.sonisori.sonisori.dto.CustomOAuth2User;
import site.sonisori.sonisori.jwt.JwtUtil;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;

	public CustomSuccessHandler(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override // onAuthenticationSuccess 메소드를 오버라이드해서 사용
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws
		IOException, ServletException {

		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();

		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String token = jwtUtil.createJwt(username, role, 60 * 60 * 60L);

		response.addCookie(createCookie("Authorization", token));
		response.sendRedirect("<http://localhost:3000/>");
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60 * 60 * 60);
		//cookie.setSecure(true);
		// -> 오직 https 에서만 쿠키 사용가능한 설정 로컬환경은 https가 아니기 때문에
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}

}

