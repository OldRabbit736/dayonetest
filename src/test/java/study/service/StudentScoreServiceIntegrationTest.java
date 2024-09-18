package study.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.IntegrationTest;
import study.MyCalculator;
import study.controller.response.ExamFailStudentResponse;
import study.controller.response.ExamPassStudentResponse;
import study.model.StudentScore;
import study.model.StudentScoreFixture;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentScoreServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private StudentScoreService studentScoreService;

    @Autowired
    private EntityManager em;

    @Test
    public void savePassedStudentScoreTest() {
        // given
        StudentScore studentScore = StudentScoreFixture.passed();

        // when
        studentScoreService.saveScore(
                studentScore.getStudentName(),
                studentScore.getExam(),
                studentScore.getKorScore(),
                studentScore.getEnglishScore(),
                studentScore.getMathScore()
        );
        em.flush();
        em.clear();

        // then
        List<ExamPassStudentResponse> passStudentList = studentScoreService.getPassStudentList(studentScore.getExam());
        assertEquals(1, passStudentList.size());

        ExamPassStudentResponse passStudent = passStudentList.get(0);
        MyCalculator calculator = new MyCalculator(0.0);
        assertEquals(studentScore.getStudentName(), passStudent.getStudentName());
        assertEquals(
                calculator
                        .add(studentScore.getKorScore().doubleValue())
                        .add(studentScore.getEnglishScore().doubleValue())
                        .add(studentScore.getMathScore().doubleValue())
                        .divide(3.0)
                        .getResult(),
                passStudent.getAvgScore()
        );
    }

    @Test
    public void saveFailedStudentScoreTest() {
        // given
        StudentScore studentScore = StudentScoreFixture.failed();

        // when
        studentScoreService.saveScore(
                studentScore.getStudentName(),
                studentScore.getExam(),
                studentScore.getKorScore(),
                studentScore.getEnglishScore(),
                studentScore.getMathScore()
        );
        em.flush();
        em.clear();

        // then
        List<ExamFailStudentResponse> failStudentList = studentScoreService.getFailStudentList(studentScore.getExam());
        assertEquals(1, failStudentList.size());

        ExamFailStudentResponse failStudent = failStudentList.get(0);
        MyCalculator calculator = new MyCalculator(0.0);
        assertEquals(studentScore.getStudentName(), failStudent.getStudentName());
        assertEquals(
                calculator
                        .add(studentScore.getKorScore().doubleValue())
                        .add(studentScore.getEnglishScore().doubleValue())
                        .add(studentScore.getMathScore().doubleValue())
                        .divide(3.0)
                        .getResult(),
                failStudent.getAvgScore()
        );
    }
}
