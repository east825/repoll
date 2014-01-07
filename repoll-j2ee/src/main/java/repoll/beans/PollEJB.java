package repoll.beans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.entities.Poll;
import repoll.entities.User;

import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class PollEJB extends BaseEJB<Poll>{
    @Override
    public Class<Poll> getEntityClass() {
        return Poll.class;
    }

    @NotNull
    public List<Poll> findByTitleFuzzy(@NotNull String query) {
        // TODO: make search more robust, at least case insensitive
        return runNamedQueryReturnMultiple(Poll.FIND_BY_TITLE_FUZZY, "title", "%" + query + "%");
    }

    @Nullable
    public Poll findByTitle(@NotNull String title) {
        return runNamedQueryReturnSingle(Poll.FIND_BY_TITLE, "title", title);
    }

    @Override
    public void persist(@NotNull Poll poll) {
        super.persist(poll);
        User author = poll.getAuthor();
        if (author != null) {
            author.getPolls().add(poll);
        }
    }
}
