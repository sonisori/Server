package site.sonisori.sonisori.auth.oauth2;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.oauth2.dto.KakaoResponse;
import site.sonisori.sonisori.auth.oauth2.dto.NaverResponse;
import site.sonisori.sonisori.auth.oauth2.dto.OAuth2Response;
import site.sonisori.sonisori.auth.oauth2.dto.OAuth2UserDto;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.enums.Role;
import site.sonisori.sonisori.common.enums.SocialType;
import site.sonisori.sonisori.entity.User;
import site.sonisori.sonisori.exception.AlreadyExistException;
import site.sonisori.sonisori.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomOAuth2Service extends DefaultOAuth2UserService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = getOAuth2Response(
			registrationId,
			oAuth2User
		);

		if (oAuth2Response == null) {
			throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
		}

		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		SocialType socialType = SocialType.valueOf(oAuth2Response.getProvider());
		Role role = Role.ROLE_USER;

		OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(
			oAuth2Response.getProviderId(),
			username,
			role,
			oAuth2Response.getEmail(),
			socialType
		);

		User user = getUserOrRegister(oAuth2Response, username);

		return new CustomOAuth2User(oAuth2UserDto, user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));

		OAuth2UserDto userDto = new OAuth2UserDto(
			user.getName(),
			user.getUsername(),
			user.getRole(),
			user.getEmail(),
			user.getSocialType()
		);

		return new CustomOAuth2User(userDto, user);
	}

	private OAuth2Response getOAuth2Response(String registrationId, OAuth2User oAuth2User) {
		switch (registrationId) {
			case "kakao":
				return new KakaoResponse(oAuth2User.getAttributes());
			case "naver":
				return new NaverResponse(oAuth2User.getAttributes());
			default:
				return null;
		}
	}

	private User getUserOrRegister(OAuth2Response oAuth2Response, String username) {
		return userRepository.findByUsername(username)
			.orElseGet(() -> registerNewUser(oAuth2Response, username));
	}

	private User registerNewUser(OAuth2Response oAuth2Response, String username) {
		if (userRepository.existsByEmail(oAuth2Response.getEmail())) {
			throw new AlreadyExistException(ErrorMessage.DUPLICATE_EMAIL.getMessage());
		}

		User user = new User();
		user.signUpOAuth2(
			username,
			oAuth2Response.getName(),
			oAuth2Response.getEmail(),
			SocialType.valueOf(oAuth2Response.getProvider())
		);

		return userRepository.save(user);
	}
}
