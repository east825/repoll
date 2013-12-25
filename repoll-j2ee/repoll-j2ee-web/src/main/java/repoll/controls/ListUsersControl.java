package repoll.controls;

import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Model
public class ListUsersControl {

    @PersistenceContext
    EntityManager manager;

    public String findAllUsers() {
        return "users.xhtml";
    }
}
