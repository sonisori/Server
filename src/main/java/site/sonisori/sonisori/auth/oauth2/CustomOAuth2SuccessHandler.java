package site.sonisori.sonisori.auth.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.cookie.CookieUtil;
import site.sonisori.sonisori.auth.jwt.JwtUtil;
import site.sonisori.sonisori.auth.jwt.dto.TokenDto;
import site.sonisori.sonisori.entity.User;

@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final CookieUtil cookieUtil;
	private final JwtUtil jwtUtil;

	@Value("${redirect.url}")
	private String redirectUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();
		User user = customUserDetails.getUser();

		TokenDto tokenDto = jwtUtil.generateJwt(user);
		String cookie = cookieUtil.createCookie("access_token", tokenDto.accessToken(), "localhost").toString();
		response.addHeader("Set-Cookie", cookie);

		cookie = cookieUtil.createCookie("refresh_token", tokenDto.refreshToken(), "localhost").toString();
		response.addHeader("Set-Cookie", cookie);

		response.sendRedirect(redirectUrl);
	}
}
