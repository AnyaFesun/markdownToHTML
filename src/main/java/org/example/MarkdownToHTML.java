package org.example;

import java.io.*;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class MarkdownToHTML {
    public MarkdownToHTML(String inputFile, String outputFile)  throws IOException, InvalidTextException {
        // Читання вмісту вхідного файлу
        StringBuilder markdownContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                markdownContent.append(line).append("\n");
            }
        }

        // Перетворення Markdown у HTML
        String htmlContent = convertMarkdownToHTML(markdownContent.toString());

        // Виведення або запис HTML
        if (outputFile == null) {
            System.out.println(htmlContent);
        } else {
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                writer.println(htmlContent);
            }
        }
    }

    private static String convertMarkdownToHTML(String markdown) throws InvalidTextException {
        StringBuilder HTMLContent = new StringBuilder();

        String prePattern = "```\\n([^`]*)```"; // preformatted text
        String boldPattern = "(?<!\\*\\*)\\*\\*(?<!\\s)(.+?)(?<!\\s)\\*\\*"; // bold
        String italicPattern = "(?<!\\S)_([^_]+)_(?!\\S)"; // italic
        String monospacePattern = "`([^`]+)`"; // monospaced

        if ((markdown.split("```").length - 1) % 2 != 0 ) {
            throw new InvalidTextException("Error: Incorrect data format!");
        }
        markdown = markdown.replaceAll(prePattern, "<pre>$1</pre>");

        //розділення текту на частинки відповідно до preformatted text
        List<String> strings = splitString(markdown);
        for (String str : strings) {
            if(!str.contains("<pre>")){

                if (str.contains("**`") || str.contains("**_") ||
                        str.contains("_**") || str.contains("_`") ||
                        str.contains("`**`") || str.contains("`_") ) {
                    throw new InvalidTextException("Error: Incorrect data format!");
                }

                Pattern pattern = Pattern.compile("\\b\\w*_[^_\\s]+\\w*\\b");
                Matcher matcher = pattern.matcher(str);

                if ((str.split("(?<!\\S)(\\*{2})(?!\\s)").length - 1 == str.split("(?<!\\s)(\\*{2})").length - 1 &&
                        str.split("(?<!\\S)(_)(?!\\s)").length - 1 == str.split("(?<!\\s)(_)").length - 1 &&
                        str.split("(?<!\\S)(`)(?!\\s)").length - 1 == str.split("(?<!\\s)(`)").length - 1) ||
                        (str.split("(?<!\\S)(\\*{2})(?!\\s)").length - 1 == str.split("(?<!\\s)(\\*{2})").length - 1 &&
                                str.split("(?<!\\S)(_)(?!\\s)").length - 1 != str.split("(?<!\\s)(_)").length - 1 && matcher.find() &&
                                str.split("(?<!\\S)(`)(?!\\s)").length - 1 == str.split("(?<!\\s)(`)").length - 1 )
                ) {

                    str = str.replaceAll(boldPattern, "<b>$1</b>");
                    str = str.replaceAll(italicPattern, "<i>$1</i>");
                    str = str.replaceAll(monospacePattern, "<tt>$1</tt>");
                }
                else {
                    throw new InvalidTextException("Error: Incorrect data format!");
                }

                str = str.replaceAll("(?m)^\\s*$", "</p><p>");
                str = "<p>" + str.trim() + "</p>";
            }
            HTMLContent.append(str);
        }
        return HTMLContent.toString();
    }

    public static List<String> splitString(String input) {
        List<String> result = new ArrayList<>();

        int preStartIndex = input.indexOf("<pre>");
        int preEndIndex = input.indexOf("</pre>");

        while (preStartIndex != -1 && preEndIndex != -1) {
            result.add(input.substring(0, preStartIndex));

            result.add(input.substring(preStartIndex, preEndIndex + "</pre>".length()));
            input = input.substring(preEndIndex + "</pre>".length());

            preStartIndex = input.indexOf("<pre>");
            preEndIndex = input.indexOf("</pre>");
        }
        return result;
    }
}
