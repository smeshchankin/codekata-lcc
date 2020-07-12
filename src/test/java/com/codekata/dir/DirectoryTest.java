package com.codekata.dir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Stream<List<String>> paths = Stream.of(Collections.emptyList());
        process(paths);

        FSNode curr = dir.getRoot();
        assertNull(curr, "Empty path has null root");
    }

    @Test
    void testAddFileAsPath() {
        Stream<List<String>> paths = Stream.of(Collections.singletonList("file.txt"));
        process(paths);

        FSNode root = dir.getRoot();
        testFSNode(root, "file.txt", 0);
    }

    @Test
    void testAddOnePath() {
        Stream<List<String>> paths = Stream.of(Arrays.asList("root", "folder", "file.txt"));
        process(paths);

        FSNode curr = dir.getRoot();
        testFSNode(curr, "root", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "folder", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "file.txt", 0);
    }

    @Test
    public void testIncorrectRootFolders() {
        final Stream<List<String>> paths = Stream.of(
            Arrays.asList("root", "file_1.txt"),
            Arrays.asList("folder", "file_2.txt"));

        assertThrows(IllegalArgumentException.class, () -> process(paths));
    }

    @Test
    public void testOneFolderTwoFiles() {
        Stream<List<String>> paths = Stream.of(
            Arrays.asList("root", "file_1.txt"),
            Arrays.asList("root", "file_2.txt"));
        process(paths);

        FSNode root = dir.getRoot();
        testFSNode(root, "root", 2);
        testFSNode(root.getChildren().get(0), "file_1.txt", 0);
        testFSNode(root.getChildren().get(1), "file_2.txt", 0);
    }

    @Test
    public void testSamePathsWithTwoFiles() {
        Stream<List<String>> paths = Stream.of(
            Arrays.asList("root", "folder", "sub-folder", "file_1.txt"),
            Arrays.asList("root", "folder", "sub-folder", "file_2.txt"));
        process(paths);

        FSNode curr = dir.getRoot();
        testFSNode(curr, "root", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "folder", 1);

        curr = curr.getChildren().get(0);
        testFSNode(curr, "sub-folder", 2);

        testFSNode(curr.getChildren().get(0), "file_1.txt", 0);
        testFSNode(curr.getChildren().get(1), "file_2.txt", 0);
    }

    private void process(Stream<List<String>> stream) {
        stream
            .map(list -> String.join("/", list))
            .map(Paths::get)
            .forEach(dir::addPath);
    }

    private void testFSNode(FSNode node, String name, int childrenSize) {
        assertEquals(name, node.getName(), "File name testing");
        assertEquals(childrenSize, node.getChildren().size(), "Node has " + childrenSize + " children");
    }
}
