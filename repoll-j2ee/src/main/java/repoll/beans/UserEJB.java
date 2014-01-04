package repoll.beans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
public class UserEJB {
    @PersistenceContext
    private EntityManager manager;

    @NotNull
    public List<User> findAll() {
        return manager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
    }

    @Nullable
    public User findByCredentials(@NotNull String login, @NotNull String password) {
        try {
            return manager.createNamedQuery(User.FIND_BY_CREDENTIALS, User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Nullable
    public User findByLogin(@NotNull String login) {
        try {
            return manager.createNamedQuery(User.FIND_BY_LOGIN, User.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void persist(User user) {
        manager.persist(user);
    }
}
