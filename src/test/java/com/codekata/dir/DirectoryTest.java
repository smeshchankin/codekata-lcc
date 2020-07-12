package com.codekata.dir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryTest {
    private Directory dir;

    @BeforeEach
    public void init() {
        dir = new Directory();
    }

    @Test
    void testAddNullPath() {
        List<String> paths = Collections.emptyList();
        Path path = Paths.get(String.join("/", paths));
        dir.addPath(path);

        FSNode curr = dir.getRoot();
        assertNull(curr, "Empty path has null root");
    }

    @Test
    void testAddFileAsPath() {
        List<String> paths = Collections.singletonList("file.txt");
        Path path = Paths.get(String.join("/", paths));
        dir.addPath(path);

        FSNode curr = dir.getRoot();
        for (String expected : paths) {
            assertNotNull(curr, "Path can't be null");
            String actual = curr.getName();
            assertEquals(expected, actual, "Checking directory name");
            if (curr.getChildren() == null || curr.getChildren().size() != 1) {
                curr = null;
            } else {
                curr = curr.getChildren().get(0);
            }
        }
    }

    @Test
    void testAddOnePath() {
        List<String> paths = Arrays.asList("folder1", "folder2", "file.txt");
        Path path = Paths.get(String.join("/", paths));
        dir.addPath(path);

        FSNode curr = dir.getRoot();
        for (String expected : paths) {
            assertNotNull(curr, "Path can't be null");
            String actual = curr.getName();
            assertEquals(expected, actual, "Checking directory name");
            if (curr.getChildren() == null || curr.getChildren().size() != 1) {
                curr = null;
            } else {
                curr = curr.getChildren().get(0);
            }
        }
    }

    @Test
    public void testOneFolderTwoFiles() {
        Stream<List<String>> stream = Stream.of(
            Arrays.asList("root", "file_1.txt"),
            Arrays.asList("root", "file_2.txt"));

        stream.map(list -> String.join("/", list))
            .map(Paths::get)
            .forEach(dir::addPath);

        FSNode root = dir.getRoot();
        assertEquals("root", root.getName(), "Root folder has root name");
        assertEquals(2, root.getChildren().size(), "Root folder has 2 children");

        FSNode curr = root.getChildren().get(0);
        assertEquals("file_1.txt", curr.getName(), "File name testing");
        assertEquals(0, curr.getChildren().size(), "Node has " + 0 + " children");

        curr = root.getChildren().get(1);
        assertEquals("file_2.txt", curr.getName(), "File name testing");
        assertEquals(0, curr.getChildren().size(), "Node has " + 0 + " children");
    }

    @Test
    public void testSamePathsWithTwoFiles() {
        Stream<List<String>> stream = Stream.of(
            Arrays.asList("root", "folder", "sub-folder", "file_1.txt"),
            Arrays.asList("root", "folder", "sub-folder", "file_2.txt"));

        stream.map(list -> String.join("/", list))
            .map(Paths::get)
            .forEach(dir::addPath);

        FSNode root = dir.getRoot();
        assertEquals("root", root.getName(), "Root folder has root name");
        assertEquals(1, root.getChildren().size(), "Root folder has 1 children");

        FSNode curr = root.getChildren().get(0);
        assertEquals("folder", curr.getName(), "File name testing");
        assertEquals(1, curr.getChildren().size(), "Node has " + 1 + " children");

        curr = curr.getChildren().get(0);
        assertEquals("sub-folder", curr.getName(), "File name testing");
        assertEquals(2, curr.getChildren().size(), "Node has " + 2 + " children");

        FSNode file = curr.getChildren().get(0);
        assertEquals("file_1.txt", file.getName(), "File name testing");
        assertEquals(0, file.getChildren().size(), "Node has " + 0 + " children");

        file = curr.getChildren().get(1);
        assertEquals("file_2.txt", file.getName(), "File name testing");
        assertEquals(0, file.getChildren().size(), "Node has " + 0 + " children");
    }
}
