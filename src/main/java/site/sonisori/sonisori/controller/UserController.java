package site.sonisori.sonisori.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.cookie.CookieUtil;
import site.sonisori.sonisori.auth.jwt.dto.TokenDto;
import site.sonisori.sonisori.dto.user.LoginRequest;
import site.sonisori.sonisori.dto.user.SignUpRequest;
import site.sonisori.sonisori.entity.User;
import site.sonisori.sonisori.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final CookieUtil cookieUtil;

	@PostMapping("/auth/signup")
	public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		userService.signUp(signUpRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/auth/login")
	public ResponseEntity<Void> login(
		@RequestBody @Valid LoginRequest loginRequest,
		HttpServletResponse response
	) {
		User user = userService.validateUser(loginRequest);
		TokenDto tokenDto = userService.createJwt(user);

		setCookies(response, "access_token", tokenDto.accessToken());
		setCookies(response, "refresh_token", tokenDto.refreshToken());

		return ResponseEntity.ok().build();
	}

	private void setCookies(HttpServletResponse response, String tokenName, String tokenValue) {
		String cookie = cookieUtil.createCookie(tokenName, tokenValue, "localhost").toString();
		response.addHeader("Set-Cookie", cookie);
	}
}
