package repoll.beans;

import repoll.entities.User;

import java.util.List;

/**
 * @author Mikhail Golubev
 */
public interface UserViewRemote {
    List<User> selectAllUsers();
}
