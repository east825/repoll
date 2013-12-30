package repoll.controls;

import repoll.beans.UserBean;
import repoll.entities.User;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Model
public class UserListControl {

    @EJB
    UserBean bean;

    public List<User> findAllUsers() {
        return bean.findAll();
    }
}
