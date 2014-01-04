package repoll.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import java.util.TimeZone;

/**
 * @author Mikhail Golubev
 */
@FacesConverter("utcDateConverter")
public class UtcDateTimeConverter extends DateTimeConverter {
    public UtcDateTimeConverter() {
        setPattern("yyyy-MM-dd");
        setTimeZone(TimeZone.getTimeZone("UTC"));
        setType("date");
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return super.getAsObject(context, component, value);
    }
}
