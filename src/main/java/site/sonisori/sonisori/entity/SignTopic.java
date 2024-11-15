package site.sonisori.sonisori.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.sonisori.sonisori.common.DateEntity;
import site.sonisori.sonisori.common.enums.Difficulty;

@Entity
@Table(name = "sign_topics")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignTopic extends DateEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title")
	@NotBlank
	@Size(max = 45)
	private String title;

	@Column(name = "contents")
	@NotBlank
	@Size(max = 255)
	private String contents;

	@Column(name = "total_quizzes")
	@NotNull
	@Min(0)
	private int totalQuizzes;

	@Column(name = "difficulty")
	@NotNull
	@Enumerated(EnumType.STRING)
	private Difficulty difficulty;

	@OneToMany(mappedBy = "signTopic", orphanRemoval = true)
	private List<SignQuiz> signQuizzes;
}
