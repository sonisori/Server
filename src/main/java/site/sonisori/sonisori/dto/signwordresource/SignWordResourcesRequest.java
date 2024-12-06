package site.sonisori.sonisori.dto.signwordresource;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignWordResourcesRequest(
	@NotNull
	@Size(min = 1)
	List<ResourceRequest> resources
) {
}
