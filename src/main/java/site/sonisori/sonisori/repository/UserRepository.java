package site.sonisori.sonisori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.sonisori.sonisori.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
