package study;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LombokTestDataTest {

  // JUnit, Lombok 동작 확인
  @Test
  public void testDataTest() {
    TestData testData = new TestData();
    testData.setName("OR");

    assertEquals("OR", testData.getName());
  }
}
