package com.codekata;

import java.util.stream.Stream;

public class LineCounter {
    private Stream<String> lines;

    public LineCounter(Stream<String> lines) {
        this.lines = lines;
    }

    public long count() {
        return this.lines == null ? 0 :
            this.lines.filter(s -> !s.isEmpty()).count();
    }
}
