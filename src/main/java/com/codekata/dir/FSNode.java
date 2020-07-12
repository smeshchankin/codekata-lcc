package com.codekata.dir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FSNode {
    private Path path;
    private String name;
    private long lines;
    private List<FSNode> children = new ArrayList<>();

    public FSNode(Path path, String name) {
        this.path = path;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public List<FSNode> getChildren() {
        return children;
    }
}
