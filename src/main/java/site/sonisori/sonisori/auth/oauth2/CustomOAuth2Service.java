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
import site.sonisori.sonisori.common.enums.Role;
import site.sonisori.sonisori.common.enums.SocialType;
import site.sonisori.sonisori.entity.User;
import site.sonisori.sonisori.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomOAuth2Service extends DefaultOAuth2UserService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("naver")) {
			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}

		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

		OAuth2UserDto oAuth2UserDto = new OAuth2UserDto(oAuth2Response.getProviderId(), username, Role.ROLE_USER,
			oAuth2Response.getEmail(), SocialType.valueOf(oAuth2Response.getProvider()));

		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));

		return new CustomOAuth2User(oAuth2UserDto, user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));

		OAuth2UserDto userDto = new OAuth2UserDto(user.getName(), user.getUsername(), user.getRole(),
			user.getEmail(), user.getSocialType());

		return new CustomOAuth2User(userDto, user);
	}
}
