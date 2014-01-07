package repoll.beans;

import org.jetbrains.annotations.NotNull;
import repoll.entities.Commentary;
import repoll.entities.Poll;
import repoll.entities.User;

import javax.ejb.Stateless;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class CommentaryEJB extends BaseEJB<Commentary> {
    @Override
    public Class<Commentary> getEntityClass() {
        return Commentary.class;
    }

    @Override
    public void persist(@NotNull Commentary commentary) {
        super.persist(commentary);
        User author = commentary.getAuthor();
        if (author != null) {
            author.getCommentaries().add(commentary);
            manager.merge(author);
        }
        Poll poll = commentary.getPoll();
        poll.getCommentaries().add(commentary);
        manager.merge(poll);
    }
}
