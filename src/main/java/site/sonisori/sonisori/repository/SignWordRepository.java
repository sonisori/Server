package site.sonisori.sonisori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.sonisori.sonisori.entity.SignWord;

@Repository
public interface SignWordRepository extends JpaRepository<SignWord, Long> {
}
