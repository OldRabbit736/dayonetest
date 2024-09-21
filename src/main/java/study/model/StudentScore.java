package study.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "student_score")
public class StudentScore {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "student_score_id")
  private Long id;

  @Column(name = "exam")
  private String exam;

  @Column(name = "student_name")
  private String studentName;

  @Column(name = "kor_score")
  private Integer korScore;

  @Column(name = "english_score")
  private Integer englishScore;

  @Column(name = "math_score")
  private Integer mathScore;
}
