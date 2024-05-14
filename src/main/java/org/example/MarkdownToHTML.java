package org.example;

import java.io.*;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownToHTML {
    private static String convertMarkdownToHTML(String markdown) {

        String prePattern = "```\\n([^`]*)```"; // preformatted text
        String boldPattern = "(?<!\\*\\*)\\*\\*(?<!\\s)(.+?)(?<!\\s)\\*\\*"; // bold
        String italicPattern = "(?<!\\S)_([^_]+)_(?!\\S)"; // italic
        String monospacePattern = "`([^`]+)`"; // monospaced

        markdown = markdown.replaceAll(prePattern, "<pre>$1</pre>");
        markdown = markdown .replaceAll(boldPattern, "<b>$1</b>");
        markdown = markdown.replaceAll(italicPattern, "<i>$1</i>");
        markdown = markdown.replaceAll(monospacePattern, "<tt>$1</tt>");

        return markdown;
    }

}