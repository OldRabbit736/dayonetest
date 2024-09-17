package study.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import study.repository.StudentFailRepository;
import study.repository.StudentPassRepository;
import study.repository.StudentScoreRepository;

class StudentScoreServiceMockTest {

    @DisplayName("첫번째 Mock 테스트")
    @Test
    public void firstSaveScoreMockTest() {
        // given
        StudentScoreService studentScoreService = new StudentScoreService(
                Mockito.mock(StudentScoreRepository.class), // mock 객체 생성
                Mockito.mock(StudentPassRepository.class),
                Mockito.mock(StudentFailRepository.class)
        );
        String givenStudentName = "OR";
        String givenExam = "testexam";
        int givenKorScore = 80;
        int givenEnglishScore = 100;
        int givenMathScore = 60;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );
    }
}
