package com.codekata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

class LineCounterTest {

  private static final Stream<String> EMPTY_STREAM = Stream.of();
  private static final Stream<String> STREAM_WITH_EMPTY_LINES = Stream.of(
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
  private static final Stream<String> STREAM_WITH_ONE_LINE_COMMENTS = Stream.of(
      "// This is first comment",
      "public class App {",
      "    // This is main method",
      "    public static void main() {",
      "        // Print string",
      "        System.out.println(\"Hello App\")",
      "    } // End method main()",
      "} // end class App");
  private static final Stream<String> STREAM_WITH_MULTI_LINE_COMMENTS = Stream.of(
      "/*****",
      " * This is a test program with 0 lines of code",
      " *  \\/* no nesting allowed!",
      " //*****//***/// Slightly pathological comment ending..."
  );
  private static final Stream<String> STREAM_WITH_TEXT_BLOCK = Stream.of(
      "public static Java13 {",
      "    //This is Java 13+ feature: text blocks",
      "    public static final void main(String [] args) {",
      "        /* Next lines are assign multiline text block to the variable */",
      "        String msg = \"\"\"This is a long",
      "        /*multiline text*/",
      "        A new Java 13 feature!\"\"\";",
      "    }",
      "}"
  );

  private final Stream<String> STREAM_WITH_COMMENTS_INSIDE_STRING = Stream.of(
    "/*****",
    " * This is a test program with 5 lines of code",
    " *  \\/* no nesting allowed!",
    " //*****//***/// Slightly pathological comment ending...",
    "",
    "public class Hello {",
    "    public static final void main(String [] args) { // gotta love Java",
    "        // Say hello",
    "      System./*wait*/out./*for*/println/*it*/(\"Hello/*\");",
    "    }",
    "",
    "}"
  );
  private final boolean[] RESULT_IS_COMMENTS_ONLY_INSIDE_STRING
      = { true, true, true, true, false, false, false, true, false, false, false, false };

  @Test
  public void testIsSingleLineCommentEmpty() {
    LineCounter lineCounter = new LineCounter(null);
    boolean isComment = lineCounter.isCommentsOnly("");
    assertFalse(isComment, "Empty message isn't a comment");
  }

  @Test
  public void testIsSingleLineCommentTrue() {
    LineCounter lineCounter = new LineCounter(null);
    boolean isComment = lineCounter.isCommentsOnly("   \t// My comment");
    assertTrue(isComment, "Comment with whitespaces");
  }

  @Test
  public void testIsMultiLineCommentTrue() {
    final LineCounter lineCounter = new LineCounter(null);
    STREAM_WITH_MULTI_LINE_COMMENTS.forEach(line -> {
      boolean isComment = lineCounter.isCommentsOnly(line);
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
    assertEquals(5, count, "One line comments should be skipped");
  }

  @Test
  public void testCount_CommentsInsideString() {
    long count = new LineCounter(STREAM_WITH_COMMENTS_INSIDE_STRING).count();
    assertEquals(5, count, "Test comments inside string");
  }

  @Test
  public void testIsCommentsOnlyInsideString() {
    final LineCounter lineCounter = new LineCounter(null);
    String[] lines = STREAM_WITH_COMMENTS_INSIDE_STRING.toArray(String[]::new);

    for (int ind = 0; ind < lines.length; ind++) {
      boolean actual = lineCounter.isCommentsOnly(lines[ind]);
      boolean expected = RESULT_IS_COMMENTS_ONLY_INSIDE_STRING[ind];
      String msg = String.format("Test isCommentOnly: %d. %s", ind, lines[ind]);
      assertEquals(expected, actual, msg);
    }
  }

  @Test
  public void testCount_Java13_MultilineTextBlock() {
    long count = new LineCounter(STREAM_WITH_TEXT_BLOCK).count();
    assertEquals(7, count, "Test comments inside string");
  }
}
