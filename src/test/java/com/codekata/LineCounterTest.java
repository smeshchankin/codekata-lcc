package com.codekata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  private static Stream<String> STREAM_WITH_ONE_LINE_COMMENTS = Stream.of(
      "// This is first comment",
      "public class App {",
      "    // This is main method",
      "    public static void main() {",
      "        // Print string",
      "        System.out.println(\"Hello App\")",
      "    } // End method main()",
      "} // end class App");
  private static Stream<String> STREAM_WITH_MULTI_LINE_COMMENTS = Stream.of(
      "/*****",
      " * This is a test program with 5 lines of code",
      " *  \\/* no nesting allowed!",
      " //*****//***/"
  );

  @Test
  public void testIsSingleLineCommentEmpty() {
    LineCounter lineCounter = new LineCounter(null);
    boolean isComment = lineCounter.isSingleLineComment("");
    assertFalse(isComment, "Empty message isn't a comment");
  }

  @Test
  public void testIsSingleLineCommentTrue() {
    LineCounter lineCounter = new LineCounter(null);
    boolean isComment = lineCounter.isSingleLineComment("   \t// My comment");
    assertTrue(isComment, "Comment with whitespaces");
  }

  @Test
  public void testIsMultiLineCommentTrue() {
    final LineCounter lineCounter = new LineCounter(null);
    STREAM_WITH_MULTI_LINE_COMMENTS.forEach(line -> {
      boolean isComment = lineCounter.isMultilineComments(line);
      assertTrue(isComment, "Multiline comments: [" + line + "]");
    });
  }

  @Test
  public void testCount_NullStream() {
    long count = new LineCounter(null).count();
    assertEquals(0, count, "Null Stream has 0 lines");
  }

  @Test
  public void testCount_EmptyStream() {
    long count = new LineCounter(EMPTY_STREAM).count();
    assertEquals(0, count, "Empty file has 0 lines");
  }

  @Test
  public void testCount_StreamWithEmptyLines() {
    long count = new LineCounter(STREAM_WITH_EMPTY_LINES).count();
    assertEquals(6, count, "Stream should skip empty lines");
  }

  @Test
  public void testFilter_StreamWithOneLineComments() {
    long count = new LineCounter(STREAM_WITH_ONE_LINE_COMMENTS).count();
    assertEquals(count, 5, "One line comments should be skipped");
  }
}
