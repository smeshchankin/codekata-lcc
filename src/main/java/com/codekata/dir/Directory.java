package com.codekata.dir;

import java.nio.file.Path;
import java.util.Objects;

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
                curr = root = new FSNode(name);
            } else if (ind == 0) {
                if (!Objects.equals(curr.getName(), name)) {
                    throw new IllegalArgumentException("First folder in path should be the same");
                }
            } else {
                curr = curr.addChild(name);
            }
        }
    }

    public FSNode getRoot() {
        return root;
    }
}
