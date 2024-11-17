package site.sonisori.sonisori.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.oauth2.dto.OAuth2UserDto;
import site.sonisori.sonisori.common.enums.Role;
import site.sonisori.sonisori.entity.User;

@RequiredArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails {
	private final OAuth2UserDto oAuth2UserDto;
	@Getter
	private final User user;
	private Map<String, Object> attributes;

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Role role = oAuth2UserDto.role();
		return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getName() {
		return oAuth2UserDto.name();
	}

	public String getUsername() {
		return oAuth2UserDto.username();
	}

	public Long getUserId() {
		return user.getId();
	}
}
