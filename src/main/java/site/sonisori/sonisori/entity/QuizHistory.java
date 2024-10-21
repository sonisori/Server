package site.sonisori.sonisori.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.sonisori.sonisori.common.DataEntity;

@Entity
@Table(name = "quiz_histories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizHistory extends DataEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "sign_topic_id")
	private SignTopic signTopic;

	@NotNull
	@Column(name = "count")
	private int count;
}
