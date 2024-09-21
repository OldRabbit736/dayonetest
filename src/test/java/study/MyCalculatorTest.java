package study;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MyCalculatorTest {

  @Test
  void addTest() {
    // AAA 패턴

    // Arrange - 준비
    MyCalculator myCalculator = new MyCalculator();

    // Act - 행동
    myCalculator.add(10.0);

    // Assert - 검증
    assertEquals(10.0, myCalculator.getResult());
  }

  @Test
  void minus() {
    // GWT 패턴

    // Given - 준비 - Arrange
    MyCalculator myCalculator = new MyCalculator(10.0);

    // When - 행동 - Act
    myCalculator.minus(5.0);

    // Then - 검증 - Assert
    assertEquals(5.0, myCalculator.getResult());
  }

  @Test
  void multiply() {
    // given
    MyCalculator myCalculator = new MyCalculator(2.0);

    // when
    myCalculator.multiply(2.0);

    // then
    assertEquals(4.0, myCalculator.getResult());
  }

  @Test
  void divide() {
    MyCalculator myCalculator = new MyCalculator(10.0);

    myCalculator.divide(2.0);

    assertEquals(5.0, myCalculator.getResult());
  }

  @Test
  void complicatedCalculateTest() {
    // given
    MyCalculator myCalculator = new MyCalculator();

    // when
    Double result = myCalculator.add(10.0).minus(4.0).multiply(2.0).divide(3.0).getResult();

    // then
    assertEquals(4.0, result);
  }

  @Test
  void divideZero() {
    // given
    MyCalculator myCalculator = new MyCalculator(10.0);

    // when, then
    assertThrows(MyCalculator.ZeroDivisionException.class, () -> myCalculator.divide(0.0));
  }
}
