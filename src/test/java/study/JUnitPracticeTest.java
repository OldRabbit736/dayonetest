package study;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JUnitPracticeTest {

    @Test
    public void assertEqualsTest() {
        String expect = "Something";
        String actual = "Something";

        assertEquals(expect, actual);
    }

    @Test
    public void assertNotEqualsTest() {
        String expect = "Something";
        String actual = "Hello";

        assertNotEquals(expect, actual);
    }

    @Test
    public void assertTrueTest() {
        int a = 10;
        int b = 10;

        assertTrue(a == b);
    }

    @Test
    public void assertFalseTest() {
        int a = 10;
        int b = 20;

        assertFalse(a == b);
    }

    @Test
    public void assertThrowsTest() {
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("");
        });
    }

    @Test
    public void assertNotNullTest() {
        String value = "Hello";
        assertNotNull(value);
    }

    @Test
    public void assertNullTest() {
        String value = null;
        assertNull(value);
    }

    @Test
    public void assertIterableEqualsTest() {
        List<Integer> list1 = List.of(1, 2);
        List<Integer> list2 = List.of(1, 2);

        assertIterableEquals(list1, list2);
    }

    @Test
    public void assertAllTest() {
        String expect = "Something";
        String actual = "Something";

        List<Integer> list1 = List.of(1, 2);
        List<Integer> list2 = List.of(1, 2);

        assertAll("Assert All", List.of(
                () -> assertEquals(expect, actual),
                () -> assertIterableEquals(list1, list2)
        ));
    }

}
