package repoll.controls;

import repoll.entities.User;

/**
 * @author Mikhail Golubev
 */
public class UserEditControl {
    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
