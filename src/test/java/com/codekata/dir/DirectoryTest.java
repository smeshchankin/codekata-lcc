package com.codekata.dir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        FSNode root = dir.getRoot();
        testFSNode(root, "file.txt", 0);
    }

    @Test
    void testAddOnePath() {
        List<String> paths = Arrays.asList("root", "folder", "file.txt");
        Path path = Paths.get(String.join("/", paths));
        dir.addPath(path);

        FSNode curr = dir.getRoot();
        testFSNode(curr, "root", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "folder", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "file.txt", 0);
    }

    @Test
    public void testIncorrectRootFolders() {
        Stream<List<String>> stream = Stream.of(
            Arrays.asList("root", "file_1.txt"),
            Arrays.asList("folder", "file_2.txt"));

        assertThrows(IllegalArgumentException.class, () ->
            stream.map(list -> String.join("/", list))
                .map(Paths::get)
                .forEach(dir::addPath)
        );
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
        testFSNode(root, "root", 2);
        testFSNode(root.getChildren().get(0), "file_1.txt", 0);
        testFSNode(root.getChildren().get(1), "file_2.txt", 0);
    }

    @Test
    public void testSamePathsWithTwoFiles() {
        Stream<List<String>> stream = Stream.of(
            Arrays.asList("root", "folder", "sub-folder", "file_1.txt"),
            Arrays.asList("root", "folder", "sub-folder", "file_2.txt"));

        stream.map(list -> String.join("/", list))
            .map(Paths::get)
            .forEach(dir::addPath);

        FSNode curr = dir.getRoot();
        testFSNode(curr, "root", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "folder", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "sub-folder", 2);

        testFSNode(curr.getChildren().get(0), "file_1.txt", 0);
        testFSNode(curr.getChildren().get(1), "file_2.txt", 0);
    }

    private void testFSNode(FSNode node, String name, int childrenSize) {
        assertEquals(name, node.getName(), "File name testing");
        assertEquals(childrenSize, node.getChildren().size(), "Node has " + childrenSize + " children");
    }
}
