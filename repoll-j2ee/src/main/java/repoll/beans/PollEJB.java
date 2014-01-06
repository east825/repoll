package repoll.beans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.entities.Poll;

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
        return runNamedQueryReturnMultiple(Poll.FIND_BY_TITLE_FUZZY, "title", "%" + query + "%");
    }

    @Nullable
    public Poll findByTitle(@NotNull String title) {
        return runNamedQueryReturnSingle(Poll.FIND_BY_TITLE, "title", title);
    }
}
