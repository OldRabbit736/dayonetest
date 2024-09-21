package study.model;

public class StudentScoreFixture {

  public static StudentScore passed() {
    return StudentScore.builder()
        .korScore(80)
        .englishScore(100)
        .mathScore(90)
        .studentName("defaultName")
        .exam("defaultExam")
        .build();
  }

  public static StudentScore failed() {
    return StudentScore.builder()
        .korScore(50)
        .englishScore(40)
        .mathScore(30)
        .studentName("defaultName")
        .exam("defaultExam")
        .build();
  }
}
