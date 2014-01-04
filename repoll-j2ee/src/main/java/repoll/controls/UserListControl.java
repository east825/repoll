package repoll.controls;

import repoll.beans.UserEJB;
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
    UserEJB bean;

    public List<User> findAllUsers() {
        return bean.findAll();
    }
}
