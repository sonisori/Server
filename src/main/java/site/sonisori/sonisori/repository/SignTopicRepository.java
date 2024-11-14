package site.sonisori.sonisori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.sonisori.sonisori.entity.SignTopic;

@Repository
public interface SignTopicRepository extends JpaRepository<SignTopic, Long> {
}
