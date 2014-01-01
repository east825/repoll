package repoll.controls;

import repoll.beans.UserLookup;
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
    UserLookup bean;

    public List<User> findAllUsers() {
        return bean.findAll();
    }
}
