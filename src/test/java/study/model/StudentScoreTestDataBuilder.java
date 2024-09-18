package study.model;

/**
 * test builder를 리턴한다. 장점이자 단점은 테스트에서 빌더 각 프로퍼티 값을 오버라이딩하여 빌드할 수 있다는 것이다.
 * 자유도가 높아서 유연하지만, 반대로 테스트에서 잘못된 인스턴스를 만들 위험도 있다.
 * (예: 평균 60점 이상인 StudentScore를 만들어야 하는데 잘못 입력해서 60점 미만인 인스턴스를 만드는 경우)
 * <p>
 * 만약 유연함을 잃는 대신 언제나 같은 값으로 고정하고 싶다면 Fixture를 사용할 수도 있다. StudentScoreFixture를 살펴보자.
 */
public class StudentScoreTestDataBuilder {

    public static StudentScore.StudentScoreBuilder passed() {
        return StudentScore
                .builder()
                .korScore(80)
                .englishScore(100)
                .mathScore(90)
                .studentName("defaultName")
                .exam("defaultExam");
    }

    public static StudentScore.StudentScoreBuilder failed() {
        return StudentScore
                .builder()
                .korScore(50)
                .englishScore(40)
                .mathScore(30)
                .studentName("defaultName")
                .exam("defaultExam");
    }
}
