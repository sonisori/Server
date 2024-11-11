package site.sonisori.sonisori.auth.oauth2.dto;

import site.sonisori.sonisori.common.enums.Role;
import site.sonisori.sonisori.common.enums.SocialType;

public record OAuth2UserDto(
	String name,
	String username,
	Role role,
	String email,
	SocialType socialType
) {
}
