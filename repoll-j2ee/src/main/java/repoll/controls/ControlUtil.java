package repoll.controls;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Mikhail Golubev
 */
public class ControlUtil {

    /**
     * Utility class
     */
    private ControlUtil() {
        // empty
    }

    public static String reportError(@Nullable String id, @NotNull String message) {
        return reportMessage(FacesMessage.SEVERITY_ERROR, id, message);
    }

    public static String reportWarning(@Nullable String id, @NotNull String message) {
        return reportMessage(FacesMessage.SEVERITY_WARN, id, message);
    }

    @Nullable
    public static String getRequestParameter(@NotNull String name) {
        return getContext().getExternalContext().getRequestParameterMap().get(name);
    }

    public static void redirect(@NotNull String url) {
        try {
            getContext().getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new AssertionError("Can't perform redirect", e);
        }
    }

    @NotNull
    public static String urlEncode(@NotNull String data) {
        try {
            return URLEncoder.encode(data, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is unsupported", e);
        }
    }

    private static String reportMessage(FacesMessage.Severity severity, String id, String message) {
        FacesMessage facesMessage = new FacesMessage(severity, message, "");
        getContext().addMessage(id, facesMessage);
        return null;
    }

    private static FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    public static ProjectStage getProjectStage() {
        return FacesContext.getCurrentInstance().getApplication().getProjectStage();
    }
}
