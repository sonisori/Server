package site.sonisori.sonisori.dto.signwordresource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.sonisori.sonisori.common.enums.ResourceType;

public record SignWordResourceRequest(
	@NotNull
	ResourceType resourceType,

	@NotBlank
	@Size(max = 500)
	String resourceUrl
) {
}
