package fr.heffebaycay.intellij.formatter;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.unscramble.UnscrambleSupport;
import fr.heffebaycay.intellij.formatter.exception.UnsupportedSentryEventException;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class SentryUnscrambleSupport implements UnscrambleSupport {

    private static final Logger logger = Logger.getInstance(SentryUnscrambleSupport.class);

    SentryStackFormatter stackFormatter = new SentryStackFormatter();

    @Nullable
    @Override
    public String unscramble(Project project, String text, String logName) {
        JSONObject sentryJSON;
        try {
            sentryJSON = new JSONObject(text);
            return stackFormatter.jsonStackToString(sentryJSON);
        } catch (JSONException e) {
            logger.warn("Failed to parse Sentry JSON file", e);
            Messages.showMessageDialog(project, SentryBundle.message("sentry.error.invalid.json"), SentryBundle.message("sentry.error.title"), Messages.getErrorIcon());
            return null;
        } catch (UnsupportedSentryEventException e) {
            logger.warn("Attempted to parse a Sentry event that is not supported by this plugin", e);
            Messages.showMessageDialog(project, SentryBundle.message("sentry.error.unsupported.event"), SentryBundle.message("sentry.error.title"), Messages.getErrorIcon());
            return null;
        }

    }

    @Override
    public String getPresentableName() {
        return SentryBundle.message("sentry.presentable.name");
    }
}
