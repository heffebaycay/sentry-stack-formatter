package fr.heffebaycay.intellij.formatter;

import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;


public class SentryStackFormatterTest {

    private SentryStackFormatter sentryStackFormatter = new SentryStackFormatter();

    protected void testParseLogicForInput(String inputFilename, String expectedFilename) throws IOException {
        String jsonInput = getFileContent(inputFilename);
        String expectedOutput = getFileContent(expectedFilename);

        JSONObject jsonObject = new JSONObject(jsonInput);

        String formattedStack = sentryStackFormatter.jsonStackToString(jsonObject);

        assertEquals(expectedOutput, formattedStack);
    }

    @Test
    public void stackTrace_should_parse_success() throws Exception {
        testParseLogicForInput("sentry-event-stacktrace.json", "sentry-event-stacktrace_expected.txt");
    }

    @Test
    public void exceptionEvent_should_parse_success() throws Exception {
        testParseLogicForInput("sentry-event-exception.json", "sentry-event-exception_expected.txt");
    }

    private String getFileContent(String resourceName) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        File file = new File(url.getPath());
        String jsonINput = new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8"));
        return jsonINput;
    }

}