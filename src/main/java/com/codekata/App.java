package com.codekata;

import com.codekata.dir.Directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class App {

  public static void main(String[] args) {
    if (args == null || args.length != 1) {
      System.err.println("1 parameter is required: file / folder");
      System.out.println("Line Code Counter Application: lcc.jar");
      return;
    }

    String rootPath = args[0];

    File file = new File(rootPath);
    if (!file.exists()) {
      System.err.println("File / folder " + rootPath + " doesn't exist");
      System.out.println("Line Code Counter Application: lcc.jar");
      return;
    }

    Directory dir = new Directory();
    try (Stream<Path> paths = Files.walk(Paths.get(rootPath))) {
      paths.forEach(dir::addPath);
    } catch (IOException cause) {
      cause.printStackTrace();
    }

    dir.calculateLines();
    dir.print(System.out);
  }
}
