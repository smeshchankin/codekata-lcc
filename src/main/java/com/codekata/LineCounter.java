package com.codekata;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class LineCounter {

  private static final int STANDARD = 0;
  private static final int MULTILINE_COMMENT = 1;
  private static final int SINGLE_LINE_COMMENT = 2;
  private static final int TEXT_STRING = 3;
  // Java 13+ has multiline text blocks: *** multiline block ***
  private static final int TEXT_BLOCK = 4;

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
              .filter(line -> !line.isEmpty())
              .filter(line -> !isCommentsOnly(line))
              .count();
    }
    return count;
  }

  boolean isCommentsOnly(String line) {
    if (line == null || "".equals(line.trim())) {
      return false;
    }

    final char[] prev = new char[2];
    AtomicBoolean hasValidCode = new AtomicBoolean(false);
    // Notes: don't use .parallel() for escape incorrect characters ordering
    line.chars().forEach(c -> {
      char ch = (char) c;
      if (!Character.isWhitespace(ch)) {
        switch (ch) {
          case '*':
            if (prev[0] == '/' && this.state == STANDARD) {
              this.state = MULTILINE_COMMENT;
            }
            break;

          case '/':
            if (prev[0] == '*' && this.state == MULTILINE_COMMENT) {
              this.state = STANDARD;
            } else if (prev[0] == '/' && this.state == STANDARD) {
              this.state = SINGLE_LINE_COMMENT;
            }
            break;

          case '"':
            if (prev[0] == '"' && prev[1] == '"') {
              if (this.state == TEXT_BLOCK) {
                this.state = STANDARD;
                hasValidCode.set(true);
              } else if (this.state == STANDARD) {
                this.state = TEXT_BLOCK;
                hasValidCode.set(true);
              }
            } else if (prev[0] != '\\') {
              if (this.state == TEXT_STRING) {
                this.state = STANDARD;
                hasValidCode.set(true);
              } else if (this.state == STANDARD) {
                this.state = TEXT_STRING;
                hasValidCode.set(true);
              }
            }

          default:
            if (this.state != MULTILINE_COMMENT && this.state != SINGLE_LINE_COMMENT) {
              hasValidCode.set(true);
            }
        }
      }

      prev[1] = prev[0];
      prev[0] = ch;
    });

    if (this.state == SINGLE_LINE_COMMENT) {
      this.state = STANDARD;
    }

    return !hasValidCode.get();
  }
}
