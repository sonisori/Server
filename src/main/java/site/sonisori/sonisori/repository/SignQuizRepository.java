package site.sonisori.sonisori.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.sonisori.sonisori.entity.SignQuiz;

@Repository
public interface SignQuizRepository extends JpaRepository<SignQuiz, Long> {
	List<SignQuiz> findAllBySignTopic_id(Long topicId);
}
