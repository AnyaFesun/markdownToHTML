import org.example.InvalidTextException;
import org.example.MarkdownToHTML;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownToHTMLTest {

    @TempDir
    Path tempDir;

    @Test
    public void testConverterInvalidInputFilePath() {
        assertThrows(IOException.class, () -> new MarkdownToHTML("test.txt", null, null));
    }

    @Test
    public void testConverterInvalidInputFile(){
        assertThrows(NullPointerException.class, () -> new MarkdownToHTML(null, null, null));
    }

    @Test
    public void testMarkdownToHTMLHtmlOutput() throws IOException, InvalidTextException {
        Path inputFile = tempDir.resolve("input.md");
        Path outputFile = tempDir.resolve("output.html");

        String markdown = "This is **bold** text.";
        Files.writeString(inputFile, markdown);

        new MarkdownToHTML(inputFile.toString(), outputFile.toString(), "html");

        String expectedHTML = "<p>This is <b>bold</b> text.</p>";
        String actualHTML = Files.readString(outputFile).trim();

        assertEquals(expectedHTML, actualHTML);
    }

    @Test
    public void testMarkdownToHTMLAnsiOutput() throws IOException, InvalidTextException {
        Path inputFile = tempDir.resolve("input.md");

        String markdown = "This is **bold** text.";
        Files.writeString(inputFile, markdown);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        new MarkdownToHTML(inputFile.toString(), null, "ansi");

        String expectedANSI = "This is \u001b[1mbold\u001b[22m text.";
        String actualANSI = outContent.toString().trim();

        assertEquals(expectedANSI, actualANSI);

        System.setOut(System.out);
    }

    @Test
    public void testMarkdownToHTMLInvalidFormat() throws IOException {
        Path inputFile = tempDir.resolve("input.md");

        String markdown = "This is **bold** text.";
        Files.writeString(inputFile, markdown);

        assertThrows(InvalidTextException.class, () -> new MarkdownToHTML(inputFile.toString(), null, "pdf"));
    }

    @Test
    public void testMarkdownToHTMLInvalidTextException() throws IOException {
        Path inputFile = tempDir.resolve("input.md");
        Path outputFile = tempDir.resolve("output.html");

        String invalidMarkdown = "This is **unclosed bold text.";
        Files.writeString(inputFile, invalidMarkdown);

        assertThrows(InvalidTextException.class, () -> new MarkdownToHTML(inputFile.toString(), outputFile.toString(), "html"));
    }

    @Test
    public void testMarkdownToHTMLIOException() {
        Path nonExistentFile = tempDir.resolve("nonexistent.md");
        Path outputFile = tempDir.resolve("output.html");

        assertThrows(IOException.class, () -> new MarkdownToHTML(nonExistentFile.toString(), outputFile.toString(), "html"));
    }

    @Test
    public void testConvertMarkdownToHTMLBold() throws InvalidTextException {
        String markdown = "This is **bold** text.";
        String expectedHTML = "<p>This is <b>bold</b> text.</p>";

        assertEquals(expectedHTML, MarkdownToHTML.convertMarkdownToHTML(markdown));
    }

    @Test
    public void testConvertFormatANSIBold() throws InvalidTextException {
        String markdown = "This is **bold** text.";
        String expectedANSI = "This is \u001b[1mbold\u001b[22m text.\n";

        assertEquals(expectedANSI, MarkdownToHTML.convertFormatANSI(markdown));
    }

    @Test
    public void testInvalidTextExceptionBold() {
        String invalidMarkdown = "This is **unclosed bold text.";
        assertThrows(InvalidTextException.class, () -> MarkdownToHTML.convertMarkdownToHTML(invalidMarkdown));
    }

    @Test
    public void testConvertMarkdownToHTMLItalic() throws InvalidTextException {
        String markdown = "This is _italic_ text, snake_case.";
        String expectedHTML = "<p>This is <i>italic</i> text, snake_case.</p>";

        assertEquals(expectedHTML, MarkdownToHTML.convertMarkdownToHTML(markdown));
    }
    @Test
    public void testConvertFormatANSIItalic() throws InvalidTextException {
        String markdown = "This is _italic_ text, snake_case.";
        String expectedANSI = "This is \u001b[3mitalic\u001b[23m text, snake_case.\n";

        assertEquals(expectedANSI, MarkdownToHTML.convertFormatANSI(markdown));
    }

    @Test
    public void testInvalidTextExceptionItalic() {
        String invalidMarkdown = "This is unclosed_ italic text.";
        assertThrows(InvalidTextException.class, () -> MarkdownToHTML.convertMarkdownToHTML(invalidMarkdown));
    }

    @Test
    public void testConvertMarkdownToHTMLMonospace() throws InvalidTextException {
        String markdown = "This is `monospace` text.";
        String expectedHTML = "<p>This is <tt>monospace</tt> text.</p>";

        assertEquals(expectedHTML, MarkdownToHTML.convertMarkdownToHTML(markdown));
    }
    @Test
    public void testConvertFormatANSIMonospace() throws InvalidTextException {
        String markdown = "This is `monospace` text.";
        String expectedANSI = "This is \u001b[7mmonospace\u001b[27m text.\n";

        assertEquals(expectedANSI, MarkdownToHTML.convertFormatANSI(markdown));
    }

    @Test
    public void testInvalidTextExceptionMonospace() {
        String invalidMarkdown = "This is unclosed `monospace text.";
        assertThrows(InvalidTextException.class, () -> MarkdownToHTML.convertMarkdownToHTML(invalidMarkdown));
    }

    @Test
    public void testConvertMarkdownToHTMLPreformatted() throws InvalidTextException {
        String markdown = "There is also a possibility\n```\nsave **fragments** of reformatted text:``` Text";
        String expectedHTML = "<p>There is also a possibility</p><pre>save **fragments** of reformatted text:</pre><p>Text</p>";

        assertEquals(expectedHTML, MarkdownToHTML.convertMarkdownToHTML(markdown));
    }
    @Test
    public void testConvertFormatANSIPreformatted() throws InvalidTextException {
        String markdown = "There is also a possibility\n```\nsave **fragments** of reformatted text:```Text";
        String expectedANSI = "There is also a possibility\n\u001b[7msave **fragments** of reformatted text:\u001b[27mText\n";

        assertEquals(expectedANSI, MarkdownToHTML.convertFormatANSI(markdown));
    }

    @Test
    public void testInvalidTextExceptionPreformatted() {
        String invalidMarkdown = "This is ```unclosed ```fragments _of_ reformatted``` text.";
        assertThrows(InvalidTextException.class, () -> MarkdownToHTML.convertMarkdownToHTML(invalidMarkdown));
    }

    @Test
    public void testConvertMarkdownToHTMLMixedFormatting() throws InvalidTextException {
        String markdown = "This is **bold**, `monospace` and _italic_ text.";
        String expectedHTML = "<p>This is <b>bold</b>, <tt>monospace</tt> and <i>italic</i> text.</p>";

        assertEquals(expectedHTML, MarkdownToHTML.convertMarkdownToHTML(markdown));
    }

    @Test
    public void testConvertFormatANSIMixedFormatting() throws InvalidTextException {
        String markdown = "This is **bold**, `monospace` and _italic_ text.";
        String expectedANSI = "This is \u001b[1mbold\u001b[22m, \u001b[7mmonospace\u001b[27m and \u001b[3mitalic\u001b[23m text.\n";

        assertEquals(expectedANSI, MarkdownToHTML.convertFormatANSI(markdown));
    }

    @Test
    public void testInvalidTextExceptionMarking() {
        String invalidMarkdown = "This **`_is invalid_`** text.";
        assertThrows(InvalidTextException.class, () -> MarkdownToHTML.convertMarkdownToHTML(invalidMarkdown));
    }

}