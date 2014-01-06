package repoll.util;

import repoll.beans.AnswerEJB;
import repoll.entities.Answer;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author Mikhail Golubev
 */
//@FacesConverter(forClass = Answer.class)
public class AnswerConverter implements Converter {
    @EJB
    private AnswerEJB answerEJB;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
//        return answerEJB.findById(Long.parseLong(value));
        Answer answer = new Answer();
        answer.setId(2);
        return answer;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(((Answer) value).getId());
    }
}
