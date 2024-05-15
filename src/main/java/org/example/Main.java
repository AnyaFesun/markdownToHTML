package org.example;

import java.io.*;
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: MarkdownToHTML <inputFile> [outputFile]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = null;

        if (args.length >= 3 && args[1].equals("--out")) {
            outputFile = args[2];
        }

        try{
            new MarkdownToHTML(inputFile, outputFile);
        }
        catch (IOException | IllegalArgumentException | InvalidTextException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}