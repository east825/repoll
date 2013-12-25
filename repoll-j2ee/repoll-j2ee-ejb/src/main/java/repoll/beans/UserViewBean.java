package repoll.beans;

import repoll.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Stateless
public class UserViewBean implements UserViewRemote {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<User> selectAllUsers() {
        Query query = manager.createNamedQuery("Users.selectAll");
        //noinspection unchecked
        return (List<User>)query.getResultList();
    }
}
