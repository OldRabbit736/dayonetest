package study.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import study.controller.response.ExamFailStudentResponse;
import study.controller.response.ExamPassStudentResponse;
import study.model.StudentFail;
import study.model.StudentFailFixture;
import study.model.StudentPass;
import study.model.StudentPassFixture;
import study.model.StudentScore;
import study.model.StudentScoreFixture;
import study.model.StudentScoreTestDataBuilder;
import study.repository.StudentFailRepository;
import study.repository.StudentPassRepository;
import study.repository.StudentScoreRepository;

// Mock을 통한 객체의 행동 검증
class StudentScoreServiceMockTest {

  private StudentScoreService studentScoreService;
  private StudentScoreRepository studentScoreRepository;
  private StudentPassRepository studentPassRepository;
  private StudentFailRepository studentFailRepository;

  @BeforeEach
  public void beforeEach() {
    studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
    studentPassRepository = Mockito.mock(StudentPassRepository.class);
    studentFailRepository = Mockito.mock(StudentFailRepository.class);
    studentScoreService =
        new StudentScoreService(
            studentScoreRepository, studentPassRepository, studentFailRepository);
  }

  @DisplayName("첫번째 Mock 테스트")
  @Test
  public void firstSaveScoreMockTest() {
    // given
    String givenStudentName = "OR";
    String givenExam = "testexam";
    int givenKorScore = 80;
    int givenEnglishScore = 100;
    int givenMathScore = 60;

    // when
    studentScoreService.saveScore(
        givenStudentName, givenExam, givenKorScore, givenEnglishScore, givenMathScore);
  }

  /**
   * 여기서 이전에 들은 "Java/Spring 테스트를 추가하고 싶은 개발자들의 오답노트" 강좌가 생각난다. StudentScoreService의 saveScore 메소드가
   * fat 하다. 이 로직을 뭔가 다른 도메인 객체의 로직으로 만들고 도메인만으로 소형 테스트 쉽게 가능하도록 만들면 좋을 거 같다.
   */
  @Test
  @DisplayName("성적 저장 로직 검증 / 60점 이상인 경우")
  public void saveScoreMockTest1() {
    // given : 평균점수가 60점 이상인 경우
    StudentScore expectStudentScore =
        StudentScoreTestDataBuilder.passed()
            .studentName("new name") // Test Data Builder 패턴은 이렇게 오버라이딩도 가능
            .build();
    StudentPass expectStudentPass = StudentPassFixture.create(expectStudentScore);

    ArgumentCaptor<StudentScore> studentScoreArgumentCaptor =
        ArgumentCaptor.forClass(StudentScore.class);
    ArgumentCaptor<StudentPass> studentPassArgumentCaptor =
        ArgumentCaptor.forClass(StudentPass.class);

    // when
    studentScoreService.saveScore(
        expectStudentScore.getStudentName(),
        expectStudentScore.getExam(),
        expectStudentScore.getKorScore(),
        expectStudentScore.getEnglishScore(),
        expectStudentScore.getMathScore());

    // then
    // studentScoreRepository.save가 한번 호출되어야 함을 검증
    Mockito.verify(studentScoreRepository, Mockito.times(1))
        .save(studentScoreArgumentCaptor.capture());
    // studentScoreRepository.save에 전달된 인수를 ArgumentCaptor를 이용해 상세 검사
    StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();
    assertEquals(expectStudentScore.getStudentName(), capturedStudentScore.getStudentName());
    assertEquals(expectStudentScore.getExam(), capturedStudentScore.getExam());
    assertEquals(expectStudentScore.getKorScore(), capturedStudentScore.getKorScore());
    assertEquals(expectStudentScore.getEnglishScore(), capturedStudentScore.getEnglishScore());
    assertEquals(expectStudentScore.getMathScore(), capturedStudentScore.getMathScore());

    Mockito.verify(studentPassRepository, Mockito.times(1))
        .save(studentPassArgumentCaptor.capture());
    StudentPass capturedStudentPass = studentPassArgumentCaptor.getValue();
    assertEquals(expectStudentPass.getStudentName(), capturedStudentPass.getStudentName());
    assertEquals(expectStudentPass.getExam(), capturedStudentPass.getExam());
    assertEquals(expectStudentPass.getAvgScore(), capturedStudentPass.getAvgScore());

    Mockito.verify(studentFailRepository, Mockito.times(0)).save(Mockito.any());
  }

  @Test
  @DisplayName("성적 저장 로직 검증 / 60점 미만인 경우")
  public void saveScoreMockTest2() {
    // given : 평균점수가 60점 미만인 경우
    StudentScore expectStudentScore = StudentScoreFixture.failed();
    StudentFail expectStudentFail = StudentFailFixture.create(expectStudentScore);

    ArgumentCaptor<StudentScore> studentScoreArgumentCaptor =
        ArgumentCaptor.forClass(StudentScore.class);
    ArgumentCaptor<StudentFail> studentFailArgumentCaptor =
        ArgumentCaptor.forClass(StudentFail.class);

    // when
    studentScoreService.saveScore(
        expectStudentScore.getStudentName(),
        expectStudentScore.getExam(),
        expectStudentScore.getKorScore(),
        expectStudentScore.getEnglishScore(),
        expectStudentScore.getMathScore());

    // then
    // studentScoreRepository.save가 한번 호출되어야 함을 검증
    Mockito.verify(studentScoreRepository, Mockito.times(1))
        .save(studentScoreArgumentCaptor.capture());
    // studentScoreRepository.save에 전달된 인수를 ArgumentCaptor를 이용해 상세 검사
    StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();
    assertEquals(expectStudentScore.getStudentName(), capturedStudentScore.getStudentName());
    assertEquals(expectStudentScore.getExam(), capturedStudentScore.getExam());
    assertEquals(expectStudentScore.getKorScore(), capturedStudentScore.getKorScore());
    assertEquals(expectStudentScore.getEnglishScore(), capturedStudentScore.getEnglishScore());
    assertEquals(expectStudentScore.getMathScore(), capturedStudentScore.getMathScore());

    Mockito.verify(studentPassRepository, Mockito.times(0)).save(Mockito.any());

    Mockito.verify(studentFailRepository, Mockito.times(1))
        .save(studentFailArgumentCaptor.capture());
    StudentFail capturedStudentFail = studentFailArgumentCaptor.getValue();
    assertEquals(expectStudentFail.getStudentName(), capturedStudentFail.getStudentName());
    assertEquals(expectStudentFail.getExam(), capturedStudentFail.getExam());
    assertEquals(expectStudentFail.getAvgScore(), capturedStudentFail.getAvgScore());
  }

  @Test
  @DisplayName("합격자 명단 가져오기 검증")
  public void getPassStudentList() {
    // given
    String givenTestExam = "testexam";
    StudentPass expectStudent1 = StudentPassFixture.create("OR1", givenTestExam);
    StudentPass expectStudent2 = StudentPassFixture.create("OR2", givenTestExam);
    StudentPass notExpectStudent3 = StudentPassFixture.create("OR3", "another exam");

    // stub 만드는 것인 듯 하다. 정해진 값만 리턴하니 말이다.
    Mockito.when(studentPassRepository.findAll())
        .thenReturn(List.of(expectStudent1, expectStudent2, notExpectStudent3));

    // when
    List<ExamPassStudentResponse> expectResponses =
        Stream.of(expectStudent1, expectStudent2)
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
    String givenTestExam = "testexam";
    StudentFail expectStudent1 = StudentFailFixture.create("OR1", givenTestExam);
    StudentFail expectStudent2 = StudentFailFixture.create("OR2", givenTestExam);
    StudentFail notExpectStudent3 = StudentFailFixture.create("OR3", "another exam");

    // stub 만드는 것인 듯 하다. 정해진 값만 리턴하니 말이다.
    Mockito.when(studentFailRepository.findAll())
        .thenReturn(List.of(expectStudent1, expectStudent2, notExpectStudent3));

    // when
    List<ExamFailStudentResponse> expectResponses =
        Stream.of(expectStudent1, expectStudent2)
            .map(pass -> new ExamFailStudentResponse(pass.getStudentName(), pass.getAvgScore()))
            .toList();
    List<ExamFailStudentResponse> responses = studentScoreService.getFailStudentList(givenTestExam);

    // then
    assertIterableEquals(expectResponses, responses);
  }
}
