package repoll.beans;

import org.jetbrains.annotations.NotNull;
import repoll.entities.Poll;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class PollEJB {
    @PersistenceContext
    EntityManager manager;

    @NotNull
    public List<Poll> findByTitleFuzzy(@NotNull String query) {
        return manager.createNamedQuery(Poll.FIND_BY_TITLE_FUZZY, Poll.class)
                .setParameter("title", "%" + query + "%").getResultList();
    }

}
