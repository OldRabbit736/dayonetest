package study;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.model.StudentScore;
import study.model.StudentScoreFixture;
import study.repository.StudentScoreRepository;

class DayonetestApplicationTests extends IntegrationTest {

    @Autowired
    private StudentScoreRepository studentScoreRepository;

    @Autowired
    private EntityManager em;

    @Test
    void contextLoads() {
        var studentScore = StudentScoreFixture.passed();
        var savedStudentScore = studentScoreRepository.save(studentScore);

        em.flush();
        em.clear();

        StudentScore foundStudentScore = studentScoreRepository.findById(savedStudentScore.getId()).orElseThrow();

        Assertions.assertEquals(savedStudentScore.getId(), foundStudentScore.getId());
        Assertions.assertEquals(savedStudentScore.getExam(), foundStudentScore.getExam());
        Assertions.assertEquals(savedStudentScore.getStudentName(), foundStudentScore.getStudentName());
        Assertions.assertEquals(savedStudentScore.getKorScore(), foundStudentScore.getKorScore());
        Assertions.assertEquals(savedStudentScore.getEnglishScore(), foundStudentScore.getEnglishScore());
        Assertions.assertEquals(savedStudentScore.getMathScore(), foundStudentScore.getMathScore());
    }

}
