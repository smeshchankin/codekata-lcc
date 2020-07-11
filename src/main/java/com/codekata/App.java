package com.codekata;

public class App {
    private static String[] array = new String[]{"арбуз", "апрель", "абрвал", "ноябрь", null};

    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
