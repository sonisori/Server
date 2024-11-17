package site.sonisori.sonisori.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.CustomUserDetails;
import site.sonisori.sonisori.auth.cookie.CookieUtil;
import site.sonisori.sonisori.auth.jwt.JwtUtil;
import site.sonisori.sonisori.auth.jwt.dto.TokenDto;
import site.sonisori.sonisori.dto.user.AuthResponse;
import site.sonisori.sonisori.dto.user.LoginRequest;
import site.sonisori.sonisori.dto.user.SignUpRequest;
import site.sonisori.sonisori.dto.user.UpdateUserNameRequest;
import site.sonisori.sonisori.dto.user.UserProfileResponse;
import site.sonisori.sonisori.entity.User;
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

	@PostMapping("/auth/login")
	public ResponseEntity<Void> login(
		@RequestBody @Valid LoginRequest loginRequest,
		HttpServletResponse response
	) {
		User user = userService.validateUser(loginRequest);
		TokenDto tokenDto = userService.createJwt(user);

		addCookies(response, "access_token", tokenDto.accessToken());
		addCookies(response, "refresh_token", tokenDto.refreshToken());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/auth/logout")
	public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = cookieUtil.getCookieValue(request, "refresh_token");
		jwtUtil.deleteRefreshToken(refreshToken);

		deleteCookies(response, "access_token");
		deleteCookies(response, "refresh_token");

		SecurityContextHolder.clearContext();

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/users/me")
	public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
		UserProfileResponse userProfileResponse = userService.setUserProfileResponse(userDetails);
		return ResponseEntity.ok(userProfileResponse);
	}

	@PutMapping("/users/me")
	public ResponseEntity<Void> updateUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody UpdateUserNameRequest updateUserNameRequest) {

		userService.updateUserName(userDetails, updateUserNameRequest);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/auth")
	public ResponseEntity<AuthResponse> getUserAuthStatus(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Optional<User> user = Optional.ofNullable(userDetails).map(CustomUserDetails::getUser);
		AuthResponse authResponse = userService.createAuthResponse(user);
		return ResponseEntity.ok(authResponse);
	}

	private void addCookies(HttpServletResponse response, String tokenName, String tokenValue) {
		String cookie = cookieUtil.createCookie(tokenName, tokenValue, "localhost").toString();
		response.addHeader("Set-Cookie", cookie);
	}

	private void deleteCookies(HttpServletResponse response, String cookieName) {
		String cookie = cookieUtil.clearCookie(cookieName, "localhost").toString();
		response.addHeader("Set-Cookie", cookie);
	}
}
