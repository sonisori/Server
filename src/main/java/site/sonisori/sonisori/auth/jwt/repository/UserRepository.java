package site.sonisori.sonisori.auth.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.sonisori.sonisori.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
