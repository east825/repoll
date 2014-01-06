package repoll.beans;

import repoll.entities.Answer;

import javax.ejb.Stateless;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class AnswerEJB extends BaseEJB<Answer> {
    @Override
    public Class<Answer> getEntityClass() {
        return Answer.class;
    }
}
