package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.model.StudentScore;

public interface StudentScoreRepository extends JpaRepository<StudentScore, Long> {
}
