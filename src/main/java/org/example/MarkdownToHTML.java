package org.example;

import java.io.*;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class MarkdownToHTML {
    public MarkdownToHTML(String inputFile, String outputFile, String outputFormat)  throws IOException, InvalidTextException {
        StringBuilder markdownContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                markdownContent.append(line).append("\n");
            }
        }

        if(outputFile != null || outputFormat == "html"){
            String htmlContent = convertMarkdownToHTML(markdownContent.toString());
                try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                    writer.println(htmlContent);
                }
        }
        else if(outputFormat == "ansi"){
            String ansiContent = convertFormatANSI(markdownContent.toString());
            System.out.println(ansiContent);
        }
        else{
            throw new InvalidTextException("Incorrect arguments!");
        }
    }

    private static String convertMarkdownToHTML(String markdown) throws InvalidTextException {
        StringBuilder HTMLContent = new StringBuilder();

        String prePattern = "```\\n([^`]*)```";
        String boldPattern = "(?<!\\*\\*)\\*\\*(?<!\\s)(.+?)(?<!\\s)\\*\\*";
        String italicPattern = "(?<!\\S)_([^_]+)_(?!\\S)";
        String monospacePattern = "`([^`]+)`";

        if ((markdown.split("```").length - 1) % 2 != 0 ) {
            throw new InvalidTextException("Incorrect data format!");
        }
        markdown = markdown.replaceAll(prePattern, "<pre>$1</pre>");

        List<String> strings = splitString(markdown);
        for (String str : strings) {
            if(!str.contains("<pre>")){

                if (str.contains("**`") || str.contains("**_") ||
                        str.contains("_**") || str.contains("_`") ||
                        str.contains("`**`") || str.contains("`_") ) {
                    throw new InvalidTextException("Incorrect data format!");
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
                    throw new InvalidTextException("Incorrect data format!");
                }

                str = str.replaceAll("(?m)^\\s*$", "</p><p>");
                str = "<p>" + str.trim() + "</p>";
            }
            HTMLContent.append(str);
        }
        return HTMLContent.toString();
    }


    private static String convertFormatANSI(String markdown) throws InvalidTextException {
        String ansiFormat = convertMarkdownToHTML(markdown);

        Pattern prePattern = Pattern.compile("<pre>(.*?)</pre>", Pattern.DOTALL);
        Pattern boldPattern = Pattern.compile("<b>(.*?)</b>", Pattern.DOTALL);
        Pattern italicPattern = Pattern.compile("<i>(.*?)</i>", Pattern.DOTALL);
        Pattern monospacePattern = Pattern.compile("<tt>(.*?)</tt>", Pattern.DOTALL);
        Pattern paragraphPattern = Pattern.compile("<p>(.*?)</p>", Pattern.DOTALL);

        ansiFormat = ConvertTegHtmlToAnsi(ansiFormat, prePattern, "\u001b[7m", "\u001b[27m");
        ansiFormat = ConvertTegHtmlToAnsi(ansiFormat, boldPattern, "\u001b[1m", "\u001b[22m");
        ansiFormat = ConvertTegHtmlToAnsi(ansiFormat, italicPattern, "\u001b[3m", "\u001b[23m");
        ansiFormat = ConvertTegHtmlToAnsi(ansiFormat, monospacePattern, "\u001b[7m", "\u001b[27m");
        ansiFormat = ConvertTegHtmlToAnsi(ansiFormat, paragraphPattern, "", "\n");

        return ansiFormat;
    }

    private static String ConvertTegHtmlToAnsi(String ansiFormat, Pattern pattern, String ansiStart, String ansiEnd){
        Matcher matcher = pattern.matcher(ansiFormat);
        while (matcher.find()) {
            String match = matcher.group(1);
            ansiFormat = ansiFormat.replace(matcher.group(), ansiStart + match + ansiEnd);
        }
        return ansiFormat;
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

        if (!input.isEmpty()) {
            result.add(input);
        }

        return result;
    }
}
