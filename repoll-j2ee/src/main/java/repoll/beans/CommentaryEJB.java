package repoll.beans;

import repoll.entities.Commentary;

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
}
