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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sign_topics")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends Data {

	@Id
	@NotNull
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", length = 45)
	@NotNull
	private String title;

	@Column(name = "contents", length = 255)
	@NotNull
	private String contents;

	@Column(name = "difficulty")
	@NotNull
	@Enumerated(EnumType.STRING)
	private Difficulty difficulty;

	@OneToMany(mappedBy = "topic", orphanRemoval = true)
	private List<Quiz> quizzes;

	@OneToMany(mappedBy = "topic", orphanRemoval = true)
	private List<History> historyList; // User의 histories와 구분

	public enum Difficulty {
		EASY, MEDIUM, HARD
	}
}
