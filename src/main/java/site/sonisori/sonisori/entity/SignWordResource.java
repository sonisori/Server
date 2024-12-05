package site.sonisori.sonisori.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.sonisori.sonisori.common.DateEntity;
import site.sonisori.sonisori.common.enums.ResourceType;

@Entity
@Table(name = "sign_word_resources")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignWordResource extends DateEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "sign_word_id")
	private SignWord signWord;

	@Column(name = "resouce_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private ResourceType resourceType;

	@NotBlank
	@Column(name = "resource_url")
	@Size(max = 500)
	private String resourceUrl;
}
