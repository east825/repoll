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
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext.getCurrentInstance().addMessage(id, facesMessage);
        return null;
    }
}
