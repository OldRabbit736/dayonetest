package study.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import study.controller.response.ExamFailStudentResponse;
import study.controller.response.ExamPassStudentResponse;
import study.model.StudentFail;
import study.model.StudentPass;
import study.repository.StudentFailRepository;
import study.repository.StudentPassRepository;
import study.repository.StudentScoreRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// Mock을 통한 객체의 행동 검증
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

    /**
     * 여기서 이전에 들은 "Java/Spring 테스트를 추가하고 싶은 개발자들의 오답노트" 강좌가 생각난다.
     * StudentScoreService의 saveScore 메소드가 fat 하다.
     * 이 로직을 뭔가 다른 도메인 객체의 로직으로 만들고 도메인만으로 소형 테스트 쉽게 가능하도록 만들면 좋을 거 같다.
     */
    @Test
    @DisplayName("성적 저장 로직 검증 / 60점 이상인 경우")
    public void saveScoreMockTest1() {
        // given : 평균점수가 60점 이상인 경우
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
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

        // then
        Mockito.verify(studentScoreRepository, Mockito.times(1))
                .save(Mockito.any()); // studentScoreRepository.save가 한번 호출되어야 함을 검증
        Mockito.verify(studentPassRepository, Mockito.times(1))
                .save(Mockito.any()); // studentPassRepository.save가 한번 호출되어야 함을 검증
        Mockito.verify(studentFailRepository, Mockito.times(0))
                .save(Mockito.any()); // studentPassRepository.save가 호출되지 말아야 함을 검증
    }

    @Test
    @DisplayName("성적 저장 로직 검증 / 60점 미만인 경우")
    public void saveScoreMockTest2() {
        // given : 평균점수가 60점 미만인 경우
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );
        String givenStudentName = "OR";
        String givenExam = "testexam";
        int givenKorScore = 40;
        int givenEnglishScore = 20;
        int givenMathScore = 30;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );

        // then
        Mockito.verify(studentScoreRepository, Mockito.times(1))
                .save(Mockito.any()); // studentScoreRepository.save가 한번 호출되어야 함을 검증
        Mockito.verify(studentPassRepository, Mockito.times(0))
                .save(Mockito.any()); // studentPassRepository.save가 호출되지 말아야 함을 검증
        Mockito.verify(studentFailRepository, Mockito.times(1))
                .save(Mockito.any()); // studentPassRepository.save가 한번 호출되어야 함을 검증
    }

    @Test
    @DisplayName("합격자 명단 가져오기 검증")
    public void getPassStudentList() {
        // given
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentPass expectStudent1 = StudentPass.builder().id(1L).studentName("OR1").exam("testexam").avgScore(70.0).build();
        StudentPass expectStudent2 = StudentPass.builder().id(2L).studentName("OR2").exam("testexam").avgScore(80.0).build();
        StudentPass notExpectStudent3 = StudentPass.builder().id(3L).studentName("OR3").exam("secondexam").avgScore(80.0).build();

        // stub 만드는 것인 듯 하다. 정해진 값만 리턴하니 말이다.
        Mockito.when(studentPassRepository.findAll()).thenReturn(List.of(
                expectStudent1,
                expectStudent2,
                notExpectStudent3
        ));

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );
        String givenTestExam = "testexam";

        // when
        List<ExamPassStudentResponse> expectResponses = Stream.of(expectStudent1, expectStudent2)
                .map(pass -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
                .toList();
        List<ExamPassStudentResponse> responses = studentScoreService.getPassStudentList(givenTestExam);

        // then
        assertIterableEquals(expectResponses, responses);
    }

    @Test
    @DisplayName("불합격자 명단 가져오기 검증")
    public void getFailStudentList() {
        // given
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentFail expectStudent1 = StudentFail.builder().id(1L).studentName("OR1").exam("testexam").avgScore(30.0).build();
        StudentFail expectStudent2 = StudentFail.builder().id(2L).studentName("OR2").exam("testexam").avgScore(50.0).build();
        StudentFail notExpectStudent3 = StudentFail.builder().id(3L).studentName("OR3").exam("secondexam").avgScore(30.0).build();

        // stub 만드는 것인 듯 하다. 정해진 값만 리턴하니 말이다.
        Mockito.when(studentFailRepository.findAll()).thenReturn(List.of(
                expectStudent1,
                expectStudent2,
                notExpectStudent3
        ));

        StudentScoreService studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );
        String givenTestExam = "testexam";

        // when
        List<ExamFailStudentResponse> expectResponses = Stream.of(expectStudent1, expectStudent2)
                .map(pass -> new ExamFailStudentResponse(pass.getStudentName(), pass.getAvgScore()))
                .toList();
        List<ExamFailStudentResponse> responses = studentScoreService.getFailStudentList(givenTestExam);

        // then
        assertIterableEquals(expectResponses, responses);
    }
}
