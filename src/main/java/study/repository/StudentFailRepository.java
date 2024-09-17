package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.model.StudentFail;

public interface StudentFailRepository extends JpaRepository<StudentFail, Long> {
}
