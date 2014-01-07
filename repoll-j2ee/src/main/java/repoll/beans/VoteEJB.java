package repoll.beans;

import org.jetbrains.annotations.NotNull;
import repoll.entities.Answer;
import repoll.entities.User;
import repoll.entities.Vote;

import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class VoteEJB extends BaseEJB<Vote> {

    @NotNull
    public List<Vote> findForAnswer(@NotNull Answer answer) {
        return runNamedQueryReturnMultiple(Vote.FIND_FOR_ANSWER, "answer", answer);
    }

    @Override
    public Class<Vote> getEntityClass() {
        return Vote.class;
    }

    @Override
    public void persist(@NotNull Vote vote) {
        super.persist(vote);
        User user = vote.getAuthor();
        if (user != null) {
            user.getVotes().add(vote);
        }
        vote.getAnswer().getVotes().add(vote);
    }
}
