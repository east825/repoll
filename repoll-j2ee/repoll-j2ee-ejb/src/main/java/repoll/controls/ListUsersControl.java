package repoll.controls;

import repoll.beans.UserListBean;
import repoll.entities.User;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Model
public class ListUsersControl {

    @EJB
    UserListBean bean;

    public List<User> findAllUsers() {
        return bean.selectAllUsers();
    }
}
