package com.codekata;

import java.util.stream.Stream;

public class LineCounter {
  private final Stream<String> lines;
  private Long count = null;

  public LineCounter(Stream<String> lines) {
    this.lines = lines;
  }

  synchronized public long count() {
    if (count == null) {
      count = this.lines == null ? 0 :
          this.lines
              .filter(s -> !s.isEmpty())
              .filter(s -> !isSingleLineComment(s))
              .count();
    }
    return count;
  }

  private boolean isSingleLineComment(String str) {
    return str.trim().startsWith("//");
  }
}
