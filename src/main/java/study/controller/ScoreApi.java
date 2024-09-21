package study.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.controller.request.SaveExamScoreRequest;
import study.controller.response.ExamFailStudentResponse;
import study.controller.response.ExamPassStudentResponse;
import study.service.StudentScoreService;

@RequiredArgsConstructor
@RestController
public class ScoreApi {

  private final StudentScoreService studentScoreService;

  @PutMapping("/exam/{exam}/score")
  public void save(@PathVariable String exam, @RequestBody SaveExamScoreRequest request) {
    studentScoreService.saveScore(
        request.getStudentName(),
        exam,
        request.getKorScore(),
        request.getEnglishScore(),
        request.getMathScore());
  }

  @GetMapping("/exam/{exam}/pass")
  public List<ExamPassStudentResponse> pass(@PathVariable String exam) {
    return studentScoreService.getPassStudentList(exam);
  }

  @GetMapping("/exam/{exam}/fail")
  public List<ExamFailStudentResponse> fail(@PathVariable String exam) {
    return studentScoreService.getFailStudentList(exam);
  }
}
