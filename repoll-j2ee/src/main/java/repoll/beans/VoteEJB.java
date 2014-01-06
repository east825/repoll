package repoll.beans;

import repoll.entities.Vote;

import javax.ejb.Stateless;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class VoteEJB extends BaseEJB<Vote> {
    @Override
    public Class<Vote> getEntityClass() {
        return Vote.class;
    }
}
