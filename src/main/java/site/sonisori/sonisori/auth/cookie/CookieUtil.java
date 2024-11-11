package site.sonisori.sonisori.auth.cookie;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
	@Value("${spring.jwt.refresh-expiration}")
	private long refreshExpiration;

	public ResponseCookie createCookie(String cookieName, String cookieValue, String domain) {
		return ResponseCookie.from(cookieName, cookieValue)
			.domain(domain)
			.path("/")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.maxAge(refreshExpiration)
			.build();
	}

	public String getCookieValue(HttpServletRequest request, String cookieName) {
		if (request == null || cookieName == null) {
			throw new IllegalArgumentException();
		}
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		return Arrays.stream(cookies)
			.filter(cookie -> cookieName.equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}

	public ResponseCookie clearCookie(String cookieName, String domain) {
		return ResponseCookie.from(cookieName, "")
			.domain(domain)
			.path("/")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.maxAge(0)
			.build();
	}
}
