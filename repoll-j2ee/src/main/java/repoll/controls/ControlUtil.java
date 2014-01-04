package repoll.controls;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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

    public static String reportError(String id, String message) {
        return reportMessage(FacesMessage.SEVERITY_ERROR, id, message);
    }

    public static String reportWarning(String id, String message) {
        return reportMessage(FacesMessage.SEVERITY_WARN, id, message);
    }

    private static String reportMessage(FacesMessage.Severity severity, String id, String message) {
        FacesMessage facesMessage = new FacesMessage(severity, message, "");
        FacesContext.getCurrentInstance().addMessage(id, facesMessage);
        return null;
    }
}
