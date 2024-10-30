package site.sonisori.sonisori.dto;

public interface OAuth2Response {

	String getProvider();

	String getProviderId();

	String getEmail();

	String getName();
}
