package repoll.controls;

import repoll.beans.UserLookup;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author Mikhail Golubev
 */
@Model
public class SignInControl {
    @EJB
    UserLookup lookup;

    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String signIn() {
        if (lookup.findByCredentials(login, password) == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong password or login", ""));
            return null;
        }
        // TODO: register current user
        return "/polls/";
    }
}
