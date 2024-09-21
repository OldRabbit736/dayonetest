package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.model.StudentPass;

public interface StudentPassRepository extends JpaRepository<StudentPass, Long> {}
