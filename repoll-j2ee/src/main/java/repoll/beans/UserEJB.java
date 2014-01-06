package repoll.beans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.entities.Poll;
import repoll.entities.User;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mikhail Golubev
 */

@Named
@Stateless
public class UserEJB extends BaseEJB<User> {
    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @NotNull
    public List<User> findAll() {
        return manager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
    }

    @Nullable
    public User findByCredentials(@NotNull String login, @NotNull String password) {
        return runNamedQueryReturnSingle(User.FIND_BY_CREDENTIALS, "login", login, "password", password);
    }

    @Nullable
    public User findByLogin(@NotNull String login) {
        return runNamedQueryReturnSingle(User.FIND_BY_LOGIN, "login", login);
    }

    public boolean userVotedInPoll(@NotNull User user, @NotNull Poll poll) {
        return !manager.createQuery("select v from Vote v where v.author = :user and v.answer.poll = :poll", User.class)
                .setParameter("user", user)
                .setParameter("poll", poll)
                .getResultList().isEmpty();
    }
}
