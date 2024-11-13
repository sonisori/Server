package site.sonisori.sonisori.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.cookie.CookieUtil;
import site.sonisori.sonisori.auth.jwt.JwtUtil;
import site.sonisori.sonisori.auth.oauth2.CustomOAuth2User;
import site.sonisori.sonisori.dto.user.SignUpRequest;
import site.sonisori.sonisori.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final CookieUtil cookieUtil;
	private final JwtUtil jwtUtil;

	@PostMapping("/auth/signup")
	public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		userService.signUp(signUpRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/logout")
	public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		HttpServletRequest request, HttpServletResponse response) {
		Long userId = customOAuth2User.getUserId();
		String refreshToken = cookieUtil.getCookieValue(request, "refresh_token");
		jwtUtil.deleteRefreshToken(userId, refreshToken);

		String cookie = cookieUtil.clearCookie("access_token", "localhost").toString();
		response.addHeader("Set-Cookie", cookie);

		cookie = cookieUtil.clearCookie("refresh_token", "localhost").toString();
		response.addHeader("Set-Cookie", cookie);

		SecurityContextHolder.clearContext();

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
