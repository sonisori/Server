package site.sonisori.sonisori.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sign_words")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Word {
	@Id
	@NotNull
	@Column(name = "id")
	private Long Id;

	@NotNull
	@Column(name = "word", length = 50)
	private String word;
}
