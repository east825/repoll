package repoll.beans;

import org.jetbrains.annotations.NotNull;
import repoll.entities.Answer;
import repoll.entities.Poll;

import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class AnswerEJB extends BaseEJB<Answer> {

    @NotNull
    public List<Answer> findForPoll(@NotNull Poll poll) {
        return runNamedQueryReturnMultiple(Answer.FIND_FOR_POLL, "poll", poll);
    }

    @Override
    public Class<Answer> getEntityClass() {
        return Answer.class;
    }

    @Override
    public void persist(@NotNull Answer answer) {
        super.persist(answer);
        Poll poll = answer.getPoll();
        poll.getAnswers().add(answer);
        manager.merge(poll);
    }
}
