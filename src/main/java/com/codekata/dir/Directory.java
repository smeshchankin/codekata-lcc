package com.codekata.dir;

import com.codekata.LineCounter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Usage: create instance and execute addPath several times
 * Limitations:
 *    * root folder should be the same
 */
public class Directory {
    private FSNode root = null;

    public void addPath(Path path) {
        FSNode curr = root;
        int ind = -1;
        for (Path p : path) {
            ++ind;
            String name = p.toString();
            if ("".equals(name)) {
                continue;
            }

            if (root == null) {
                curr = root = new FSNode(Paths.get(name), name);
            } else if (ind == 0) {
                if (!Objects.equals(curr.getName(), name)) {
                    throw new IllegalArgumentException("First folder in path should be the same");
                }
            } else {
                curr = curr.addChild(name);
            }
        }
    }

    // Method is used only in tests
    FSNode getRoot() {
        return root;
    }

    public void calculateLines() {
        calculateLines(root);
    }

    private long calculateLines(FSNode node) {
        if (node.isFile()) {
            Path path = node.getPath();
            try (Stream<String> lines = Files.lines(path)) {
                LineCounter lineCounter = new LineCounter(lines);
                node.setLines(lineCounter.count());
            } catch (IOException cause) {
                System.err.println("Can't calculate line for " + path);
            } catch (UncheckedIOException cause) {
                System.err.println("Can't calculate lines for " + path);
            }
        } else if (node.isFolder()) {
            long size = 0;
            for (FSNode child : node.getChildren()) {
                size += calculateLines(child);
            }
            node.setLines(size);
        }
        return node.getLines();
    }
}
