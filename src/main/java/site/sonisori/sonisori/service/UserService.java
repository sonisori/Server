package site.sonisori.sonisori.service;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.jwt.JwtUtil;
import site.sonisori.sonisori.auth.jwt.dto.TokenDto;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.enums.Role;
import site.sonisori.sonisori.common.enums.SocialType;
import site.sonisori.sonisori.dto.user.AuthResponse;
import site.sonisori.sonisori.dto.user.LoginRequest;
import site.sonisori.sonisori.dto.user.SignUpRequest;
import site.sonisori.sonisori.dto.user.UpdateUserNameRequest;
import site.sonisori.sonisori.dto.user.UserProfileResponse;
import site.sonisori.sonisori.entity.User;
import site.sonisori.sonisori.exception.AlreadyExistException;
import site.sonisori.sonisori.exception.InvalidUserException;
import site.sonisori.sonisori.exception.NotFoundException;
import site.sonisori.sonisori.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public void signUp(SignUpRequest signUpRequest) {
		checkEmailDuplicate(signUpRequest.email());
		User user = buildUserForSignUp(signUpRequest);
		userRepository.save(user);
	}

	private void checkEmailDuplicate(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new AlreadyExistException(ErrorMessage.DUPLICATE_EMAIL.getMessage());
		}
	}

	private User buildUserForSignUp(SignUpRequest signUpRequest) {
		return User.builder()
			.name(signUpRequest.name())
			.email(signUpRequest.email())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.role(Role.ROLE_USER)
			.username(signUpRequest.email())
			.socialType(SocialType.none)
			.build();
	}

	public User validateUser(LoginRequest loginRequest) {
		String email = loginRequest.email();
		String password = loginRequest.password();
		User user = userRepository.findByEmail(email).orElseThrow(InvalidUserException::new);

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new InvalidUserException();
		}
		return user;
	}

	public TokenDto createJwt(User user) {
		return jwtUtil.generateJwt(user);
	}

	@Transactional(readOnly = true)
	public AuthResponse createAuthResponse(Optional<User> user) {
		return (user.isPresent())
			? createLoggedInUserAuthStatus(user.get()) : createAnonymousUserAuthStatus();
	}

	private AuthResponse createLoggedInUserAuthStatus(User user) {
		String name = user.getName();
		return AuthResponse.builder()
			.isLogin(true)
			.name(name)
			.build();
	}

	private AuthResponse createAnonymousUserAuthStatus() {
		return AuthResponse.builder()
			.isLogin(false)
			.name(null)
			.build();
	}

	public void updateUserName(Long userId, UpdateUserNameRequest updateUserNameRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER.getMessage()));

		user.updateName(updateUserNameRequest.name());
	}

	public UserProfileResponse createUserProfileResponse(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER.getMessage()));

		return new UserProfileResponse(user.getName(), user.getSocialType().toString());
	}

	public void deleteUser(Long userId) {
		try {
			userRepository.deleteById(userId);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_USER.getMessage());
		}
	}
}
