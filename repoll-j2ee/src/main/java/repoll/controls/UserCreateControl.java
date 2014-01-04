package repoll.controls;

import repoll.beans.UserEJB;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

/**
 * @author Mikhail Golubev
 */
@Model
public class UserCreateControl extends UserEditControl {
    @EJB
    private UserEJB userEJB;
    @Inject
    LoginControl loginControl;

    private String passwordRepeat;

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String create() {
        boolean valid = true;
        if (userEJB.findByLogin(getUser().getLogin()) != null) {
            ControlUtil.reportWarning("login", "User with this login already exists");
            valid = false;
        }
        if (!getUser().getPassword().equals(passwordRepeat)) {
            ControlUtil.reportError(null, "Passwords don't match");
            valid = false;
        }
        if (!valid) {
            return null;
        }
        loginControl.setCurrentUser(getUser());
        userEJB.persist(getUser());
        return "/polls/list.xhtml";
    }
}
