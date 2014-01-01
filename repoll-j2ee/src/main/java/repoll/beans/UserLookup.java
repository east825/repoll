package repoll.beans;

import repoll.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class UserLookup {
    @PersistenceContext
    private EntityManager manager;

    public List<User> findAll() {
        return manager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
    }

    public User findByCredentials(String login, String password) {
        try {
            return manager.createNamedQuery(User.FIND_BY_CREDENTIALS, User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
