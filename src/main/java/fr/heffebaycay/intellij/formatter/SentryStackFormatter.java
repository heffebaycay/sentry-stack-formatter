package fr.heffebaycay.intellij.formatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SentryStackFormatter {


    public String jsonStackToString(JSONObject jsonObject) {
        JSONObject mainException = jsonObject.getJSONObject("sentry.interfaces.Exception");

        JSONArray values = mainException.getJSONArray("values");

        List<FakeException> fakeExceptions = new ArrayList<>();
        for (int i = 0; i < values.length(); i++) {
            FakeException stackTraceElements = stackValueToStackTraceElement(values.getJSONObject(i));
            fakeExceptions.add(stackTraceElements);
        }

        for(int i = fakeExceptions.size() - 2; i >= 0; i--) {
            fakeExceptions.get(i).initCause(fakeExceptions.get(i + 1));
        }

        return fakeExceptions.get(0).getStringStackTrace();
    }

    protected FakeException stackValueToStackTraceElement(JSONObject stackJsonObject) {
        String type = stackJsonObject.getString("type");
        String module = stackJsonObject.getString("module");
        String value = stackJsonObject.getString("value");

        JSONArray frames = stackJsonObject.getJSONObject("stacktrace").getJSONArray("frames");
        StackTraceElement[] stackTraceElements = new StackTraceElement[frames.length()];

        for (int i = 0; i < frames.length(); i++) {
            JSONObject frame = frames.getJSONObject(i);
            String frameModule = frame.getString("module");
            String frameFunction = frame.getString("function");
            int frameLineNo = frame.has("lineno") ? frame.getInt("lineno") : -1; // -1 means no line info available

            stackTraceElements[i] = new StackTraceElement(frameModule, frameFunction, "", frameLineNo);
        }

        FakeException exception = new FakeException(module, type, value);
        exception.setStackTrace(stackTraceElements);

        return exception;
    }

}
