package site.sonisori.sonisori.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import site.sonisori.sonisori.dto.CustomOAuth2User;
import site.sonisori.sonisori.dto.GoogleResponse;
import site.sonisori.sonisori.dto.KakaoResponse;
import site.sonisori.sonisori.dto.OAuth2Response;
import site.sonisori.sonisori.dto.UserDTO;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		// System.out.println(oAuth2User); 값 확인 부분

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("google")) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}

		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		userDTO.setName(oAuth2Response.getName());
		userDTO.setRole("ROLE_USER");

		return new CustomOAuth2User(userDTO);
	}

}
