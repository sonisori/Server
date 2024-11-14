package site.sonisori.sonisori.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.sonisori.sonisori.entity.QuizHistory;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
}
