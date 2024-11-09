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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.sonisori.sonisori.common.DateEntity;
import site.sonisori.sonisori.common.enums.Role;
import site.sonisori.sonisori.common.enums.SocialType;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	@NotBlank
	@Size(max = 20)
	private String name;

	@Column(name = "email")
	@NotBlank
	@Size(max = 45)
	private String email;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	@NotNull
	private Role role;

	@Column(name = "username")
	@NotBlank
	@Size(max = 500)
	private String username;

	@Column(name = "social_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private SocialType socialType;

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<QuizHistory> quizHistories;

	public void signUp(String username, String name, String email) {
		this.username = username;
		this.name = name;
		this.email = email;
	}

}
