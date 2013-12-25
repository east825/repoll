package repoll.beans;

import repoll.entities.User;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Named
@Stateless
public class UserListBean {
    @PersistenceContext
    private EntityManager manager;

    public List<User> selectAllUsers() {
        return manager.createNamedQuery(User.FIND_BY_ID_Q, User.class).getResultList();
    }
}
