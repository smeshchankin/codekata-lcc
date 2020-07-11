package com.codekata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

class LineCounterTest {
    private static Stream<String> EMPTY_STREAM = Stream.of();
    private static Stream<String> STREAM_WITH_EMPTY_LINES = Stream.of(
            "package com.app;",
            "",
            "",
            "public class App {",
            "    public static void main(String[] args) {",
            "",
            "        System.out.println(\"Hello\");",
            "    }",
            "}",
            "");

    @Test
    void testCount_NullStream() {
        long count = new LineCounter(null).count();
        Assertions.assertEquals(0, count, "Null Stream has 0 lines");
    }

    @Test
    void testCount_EmptyStream() {
        long count = new LineCounter(EMPTY_STREAM).count();
        Assertions.assertEquals(0, count, "Empty file has 0 lines");
    }

    @Test
    void testCount_StreamWithEmptyLines() {
        long count = new LineCounter(STREAM_WITH_EMPTY_LINES).count();
        Assertions.assertEquals(6, count, "Stream should skip empty lines");
    }
}
