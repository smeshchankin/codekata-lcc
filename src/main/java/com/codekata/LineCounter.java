package com.codekata;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class LineCounter {

  private static final int STANDARD = 0;
  private static final int MULTILINE_COMMENT = 1;
  private static final int SINGLE_LINE_COMMENT = 2;
  // Java 13+ has multiline text blocks: *** multiline block ***
  private static final int TEXT_BLOCK = 3;

  private final Stream<String> lines;
  private Long count = null;
  private int state = STANDARD;

  public LineCounter(Stream<String> lines) {
    this.lines = lines;
  }

  synchronized public long count() {
    if (count == null) {
      count = this.lines == null ? 0 :
          this.lines
              .filter(s -> !s.isEmpty())
              .filter(s -> !isMultilineComments(s))
              .filter(s -> !isSingleLineComment(s))
              .count();
    }
    return count;
  }

  boolean isMultilineComments(String line) {
    final char[] prev = new char[2];
    AtomicBoolean hasValidCode = new AtomicBoolean(false);
    // Notes: don't use .parallel() for escape incorrect characters ordering
    line.chars().forEach(c -> {
      char ch = (char) c;
      if (!Character.isWhitespace(ch)) {
        switch (ch) {
          case '*':
            if (prev[0] == '/') {
              this.state = MULTILINE_COMMENT;
            } else if (prev[0] == '*' && prev[1] == '*' && this.state != MULTILINE_COMMENT) {
              this.state = this.state == TEXT_BLOCK ? STANDARD : TEXT_BLOCK;
            }
            break;
          case '/':
            if (prev[0] == '*' && this.state == MULTILINE_COMMENT) {
              this.state = STANDARD;
            }
            break;
          default:
            if (this.state != MULTILINE_COMMENT) {
              hasValidCode.set(true);
            }
        }
      }

      prev[1] = prev[0];
      prev[0] = ch;
    });

    return !hasValidCode.get();
  }

  boolean isSingleLineComment(String str) {
    return str.trim().startsWith("//");
  }
}
