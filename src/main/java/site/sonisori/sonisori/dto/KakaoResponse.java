package site.sonisori.sonisori.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

	private final Map<String, Object> attribute;
	private final Map<String, Object> attributesAccount;
	private final Map<String, Object> attributesProfile;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.attributesAccount = (Map<String, Object>)attribute.get("kakao_account");
		this.attributesProfile = (Map<String, Object>)attributesAccount.get("profile");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		return attributesAccount.get("email").toString();
	}

	@Override
	public String getName() {
		return attributesProfile.get("nickname").toString();
	}
}
