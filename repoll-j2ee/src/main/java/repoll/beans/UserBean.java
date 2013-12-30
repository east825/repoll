package repoll.beans;

import repoll.entities.User;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Named
@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager manager;

    public List<User> findAll() {
        return manager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
    }
}
