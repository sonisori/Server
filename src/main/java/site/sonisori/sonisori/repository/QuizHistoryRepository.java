package site.sonisori.sonisori.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.sonisori.sonisori.entity.QuizHistory;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
	List<QuizHistory> findByUser_id(Long userId);
}
