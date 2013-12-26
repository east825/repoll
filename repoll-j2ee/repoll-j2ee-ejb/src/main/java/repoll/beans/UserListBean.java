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
public class UserListBean {
    @PersistenceContext(unitName = "repoll-main")
    private EntityManager manager;

    public List<User> selectAllUsers() {
        return manager.createQuery("select a from User", User.class).getResultList();
    }
}
