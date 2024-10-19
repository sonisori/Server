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
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Data {
	@Id
	@Column(name = "id")
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 20)
	@NotNull
	private String name;

	@Column(name = "email", length = 45)
	@NotNull
	private String email;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	@NotNull
	private Role role;

	@Column(name = "username", length = 500)
	@NotNull
	private String username;

	@Column(name = "social_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private SocialType socialType;

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<History> histories;

	public enum Role {
		ROLE_USER, ROLE_ADMIN
	}

	public enum SocialType {
		kakao, google
	}
}
