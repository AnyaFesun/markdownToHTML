package org.example;

import java.io.*;
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <inputFile> [--out <outputFile>] [--format=<html/ansi>]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = null;
        String outputFormat = null;


        if(args.length < 2){
            outputFormat = "ansi";
        }
        else{
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("--out")) {
                    outputFile = args[i+1];
                    i++;
                } else if (args[i].startsWith("--format=")) {
                    outputFormat = args[i].substring(9);
                }
            }

            if(outputFormat != null){
                if(outputFormat.equals("html")){
                    outputFile = "./src/main/resources/output.html";
                }
            }
        }

        try{
            new MarkdownToHTML(inputFile, outputFile, outputFormat);
        }
        catch (IOException | IllegalArgumentException | InvalidTextException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}