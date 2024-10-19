package site.sonisori.sonisori.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "quiz_histories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History extends Data {

	@Id
	@NotNull
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "sign_topic_id")
	private Topic topic;

	@NotNull
	@Column(name = "count")
	private int count;
}
