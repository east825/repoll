package repoll.beans;

import org.jetbrains.annotations.NotNull;
import repoll.entities.Commentary;
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
        }
        commentary.getPoll().getCommentaries().add(commentary);
    }

    @Override
    public void remove(@NotNull Commentary commentary) {
        super.remove(commentary);
        User author = commentary.getAuthor();
        if (author != null) {
            author.getCommentaries().remove(commentary);
        }
        commentary.getPoll().getCommentaries().remove(commentary);
        System.out.println(commentary.getPoll().getCommentaries().size());
    }
}
