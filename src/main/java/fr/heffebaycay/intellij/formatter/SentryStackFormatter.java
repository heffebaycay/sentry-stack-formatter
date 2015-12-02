package fr.heffebaycay.intellij.formatter;

import fr.heffebaycay.intellij.formatter.exception.UnsupportedSentryEventException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SentryStackFormatter {


    public static final String SENTRY_EXCEPTION_INTERFACE = "sentry.interfaces.Exception";
    public static final String SENTRY_STACKTRACE_INTERFACE = "sentry.interfaces.Stacktrace";

    public String jsonStackToString(JSONObject jsonObject) {

        FakeException exception;
        if (jsonObject.has(SENTRY_EXCEPTION_INTERFACE)) {
            exception = parseExceptionEvent(jsonObject);
        } else if (jsonObject.has(SENTRY_STACKTRACE_INTERFACE)) {
            exception = parseStacktraceEvent(jsonObject);
        } else {
            throw new UnsupportedSentryEventException();
        }

        return exception.getStringStackTrace();
    }

    protected FakeException parseExceptionEvent(JSONObject exceptionEvent) {
        JSONObject mainException = exceptionEvent.getJSONObject(SENTRY_EXCEPTION_INTERFACE);
        JSONArray exceptionValues = mainException.getJSONArray("values");
        List<FakeException> fakeExceptions = new ArrayList<>();

        for (int i = 0; i < exceptionValues.length(); i++) {
            FakeException fakeException = stackValueToStackTraceElement(exceptionValues.getJSONObject(i));
            fakeExceptions.add(fakeException);
        }

        for (int i = fakeExceptions.size() - 2; i >= 0; i--) {
            fakeExceptions.get(i).initCause(fakeExceptions.get(i+1));
        }

        return fakeExceptions.get(0);
    }

    protected FakeException parseStacktraceEvent(JSONObject stacktraceEvent) {
        String value = stacktraceEvent.getString("message");
        FakeException fakeException = new FakeException(null, null, value);

        JSONObject stacktraceInterface = stacktraceEvent.getJSONObject(SENTRY_STACKTRACE_INTERFACE);
        StackTraceElement[] stackTraceElements = parseStackFrames(stacktraceInterface.getJSONArray("frames"));

        fakeException.setStackTrace(stackTraceElements);

        return fakeException;
    }

    protected StackTraceElement[] parseStackFrames(JSONArray frames) {
        StackTraceElement[] stackTraceElements = new StackTraceElement[frames.length()];

        for (int i = 0; i < frames.length(); i++) {
            JSONObject frame = frames.getJSONObject(frames.length() - i - 1);
            String frameModule = frame.getString("module");
            String frameFunction = frame.getString("function");
            int frameLineNo = frame.has("lineno") ? frame.getInt("lineno") : -1; // -1 means no line info available

            stackTraceElements[i] = new StackTraceElement(frameModule, frameFunction, "", frameLineNo);
        }

        return stackTraceElements;
    }

    protected FakeException stackValueToStackTraceElement(JSONObject stackJsonObject) {
        String type = stackJsonObject.getString("type");
        String module = stackJsonObject.getString("module");
        String value = stackJsonObject.getString("value");

        JSONArray frames = stackJsonObject.getJSONObject("stacktrace").getJSONArray("frames");
        StackTraceElement[] stackTraceElements = parseStackFrames(frames);

        FakeException exception = new FakeException(module, type, value);
        exception.setStackTrace(stackTraceElements);

        return exception;
    }

}
