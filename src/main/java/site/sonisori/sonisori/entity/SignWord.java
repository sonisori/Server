package site.sonisori.sonisori.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.sonisori.sonisori.common.DateEntity;
import site.sonisori.sonisori.dto.signword.SignWordResponse;

@Entity
@Table(name = "sign_words")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignWord extends DateEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "word")
	@Size(max = 50)
	private String word;

	@Column(name = "description")
	@Size(max = 500)
	private String description;

	@OneToMany(mappedBy = "signWord", fetch = FetchType.LAZY)
	private List<SignWordResource> signWordResources;

	public SignWordResponse toDto() {
		return new SignWordResponse(this.id, this.word);
	}

	public void updateWord(String word, String description) {
		this.word = word;
		this.description = description;
	}
}
