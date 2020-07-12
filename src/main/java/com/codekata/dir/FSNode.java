package com.codekata.dir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FSNode {
    private Path path;
    private String name;
    private long lines;
    private List<FSNode> children = new ArrayList<>();

    public FSNode(String name) {
        this.name = name;
    }

    public FSNode addChild(String name) {
        FSNode node = new FSNode(name);
        children.add(node);
        return node;
    }

    public String getName() {
        return name;
    }

    public List<FSNode> getChildren() {
        return children;
    }
}
