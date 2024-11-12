package site.sonisori.sonisori.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.enums.Role;
import site.sonisori.sonisori.common.enums.SocialType;
import site.sonisori.sonisori.dto.user.SignUpRequest;
import site.sonisori.sonisori.entity.User;
import site.sonisori.sonisori.exception.AlreadyExistException;
import site.sonisori.sonisori.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
}
