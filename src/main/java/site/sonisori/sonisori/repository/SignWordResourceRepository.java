package site.sonisori.sonisori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.sonisori.sonisori.entity.SignWordResource;

@Repository
public interface SignWordResourceRepository extends JpaRepository<SignWordResource, Long> {
}
