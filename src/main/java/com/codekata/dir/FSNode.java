package com.codekata.dir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FSNode {
    private final Path path;
    private final String name;
    private final boolean isFile;
    private final boolean isFolder;
    private long lines = 0L;
    private final List<FSNode> children = new ArrayList<>();

    public FSNode(Path path, String name) {
        this.path = path;
        this.name = name;
        this.isFile = Files.isRegularFile(path);
        this.isFolder = Files.isDirectory(path);
    }

    public FSNode addChild(String name) {
        Optional<FSNode> node = children.stream()
            .filter(n -> Objects.equals(n.name, name))
            .findFirst();
        if (node.isPresent()) {
            return node.get();
        } else {
            FSNode fsNode = new FSNode(path.resolve(name), name);
            children.add(fsNode);
            return fsNode;
        }
    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public long getLines() {
        return lines;
    }

    public void setLines(long lines) {
        this.lines = lines;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isFolder() {
        return isFolder;
    }

    // Method is used only in tests && Directory.class
    List<FSNode> getChildren() {
        return children;
    }
}
