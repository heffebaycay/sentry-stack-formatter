package fr.heffebaycay.intellij.formatter;

import java.io.PrintWriter;
import java.io.StringWriter;

public class FakeException extends Exception {

    protected String module;
    protected String type;


    public FakeException(String module, String type, String message) {
        super(message);
        this.module = module;
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(module);
        buffer.append(".");
        buffer.append(type);
        buffer.append(": ");
        buffer.append(getMessage());

        return buffer.toString();
    }

    public String getStringStackTrace() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        printStackTrace(printWriter);

        return stringWriter.toString();
    }
}
