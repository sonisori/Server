package site.sonisori.sonisori.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.sonisori.sonisori.entity.SignQuiz;

public interface SignQuizRepository extends JpaRepository<SignQuiz, Long> {
	int countBySignTopic_id(Long signTopicId);
}
