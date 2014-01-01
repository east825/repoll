package repoll.controls;

import repoll.beans.UserLookup;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Mikhail Golubev
 */
@Model
public class SignUpControl extends UserEditControl {

    @EJB
    private UserLookup lookup;
    @PersistenceContext
    EntityManager manager;

    private String passwordRepeat;

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String signUp() {
        if (lookup.findByLogin(getUser().getLogin()) != null) {
            return ControlUtil.reportError("login", "User with this login already exists");
        }
        if (!getUser().getPassword().equals(passwordRepeat)) {
            return ControlUtil.reportError(null, "Passwords don't match");
        }
        manager.persist(getUser());
        return "polls/";
    }
}
