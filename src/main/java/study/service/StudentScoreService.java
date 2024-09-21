package study.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.MyCalculator;
import study.controller.response.ExamFailStudentResponse;
import study.controller.response.ExamPassStudentResponse;
import study.model.StudentFail;
import study.model.StudentPass;
import study.model.StudentScore;
import study.repository.StudentFailRepository;
import study.repository.StudentPassRepository;
import study.repository.StudentScoreRepository;

@RequiredArgsConstructor
@Service
public class StudentScoreService {

  private final StudentScoreRepository studentScoreRepository;
  private final StudentPassRepository studentPassRepository;
  private final StudentFailRepository studentFailRepository;

  public void saveScore(
      String studentName, String exam, Integer korScore, Integer englishScore, Integer mathScore) {
    StudentScore studentScore =
        StudentScore.builder()
            .studentName(studentName)
            .exam(exam)
            .korScore(korScore)
            .englishScore(englishScore)
            .mathScore(mathScore)
            .build();
    studentScoreRepository.save(studentScore);

    MyCalculator calculator = new MyCalculator(0.0);
    Double avgScore =
        calculator
            .add(korScore.doubleValue())
            .add(englishScore.doubleValue())
            .add(mathScore.doubleValue())
            .divide(3.0)
            .getResult();

    if (avgScore >= 60) {
      StudentPass studentPass =
          StudentPass.builder().exam(exam).studentName(studentName).avgScore(avgScore).build();
      studentPassRepository.save(studentPass);
    } else {
      StudentFail studentFail =
          StudentFail.builder().exam(exam).studentName(studentName).avgScore(avgScore).build();
      studentFailRepository.save(studentFail);
    }
  }

  public List<ExamPassStudentResponse> getPassStudentList(String exam) {
    List<StudentPass> studentPassList = studentPassRepository.findAll();
    return studentPassList.stream()
        .filter(pass -> pass.getExam().equals(exam))
        .map(pass -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
        .toList();
  }

  public List<ExamFailStudentResponse> getFailStudentList(String exam) {
    List<StudentFail> studentFailList = studentFailRepository.findAll();
    return studentFailList.stream()
        .filter(fail -> fail.getExam().equals(exam))
        .map(fail -> new ExamFailStudentResponse(fail.getStudentName(), fail.getAvgScore()))
        .toList();
  }
}
