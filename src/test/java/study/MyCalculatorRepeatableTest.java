package study;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MyCalculatorRepeatableTest {

  @RepeatedTest(5)
  public void repeatedAddTest() {
    // Arrange - 준비
    MyCalculator myCalculator = new MyCalculator();

    // Act - 행동
    myCalculator.add(10.0);

    // Assert - 검증
    assertEquals(10.0, myCalculator.getResult());
  }

  @ParameterizedTest
  @MethodSource(value = "parameterizedTestParameters")
  public void parameterizedTest(Double addValue, Double expectedValue) {
    // Arrange - 준비
    MyCalculator myCalculator = new MyCalculator(0.0);

    // Act - 행동
    myCalculator.add(addValue);

    // Assert - 검증
    assertEquals(expectedValue, myCalculator.getResult());
  }

  public static Stream<Arguments> parameterizedTestParameters() {
    return Stream.of(Arguments.of(10.0, 10.0), Arguments.of(8.0, 8.0), Arguments.of(17.0, 17.0));
  }

  @ParameterizedTest
  @MethodSource("parameterizedComplicatedCalculateTestParameters")
  public void parameterizedComplicatedCalculateTest(
      Double addValue,
      Double minusValue,
      Double multiplyValue,
      Double divideValue,
      Double expectValue) {
    // given
    MyCalculator myCalculator = new MyCalculator();

    // when
    Double result =
        myCalculator
            .add(addValue)
            .minus(minusValue)
            .multiply(multiplyValue)
            .divide(divideValue)
            .getResult();

    // then
    assertEquals(expectValue, result);
  }

  public static Stream<Arguments> parameterizedComplicatedCalculateTestParameters() {
    return Stream.of(Arguments.of(10.0, 4.0, 2.0, 3.0, 4.0), Arguments.of(4.0, 2.0, 4.0, 4.0, 2.0));
  }
}
