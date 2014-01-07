package repoll.controls;

import org.jetbrains.annotations.Nullable;
import repoll.beans.UserEJB;
import repoll.entities.User;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ProjectStage;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mikhail Golubev
 */
@Named
@SessionScoped
public class LoginControl implements Serializable {
    @EJB
    private UserEJB userEJB;
    private User currentUser;

    @NotNull
    private String login;
    @NotNull
    private String password;

    @PostConstruct
    private void createTestUser() {
        if (ControlUtil.getProjectStage() == ProjectStage.Development) {
            currentUser = userEJB.findByLogin("east825");
        }
    }

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

    public String login() {
        User user = userEJB.findByLogin(login);
        if (user == null || !user.getPassword().equals(password)) {
            return ControlUtil.reportError(null, "Wrong password or login");
        }
        setCurrentUser(user);
        return "/polls/list.xhtml";
    }

    public String logout() {
        currentUser = null;
        return "/login.xhtml";
    }

    public boolean userAuthorised() {
        return currentUser != null;
    }

    @Nullable
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(@org.jetbrains.annotations.NotNull User user) {
        user.setLastVisitDate(new Date());
        currentUser = user;
        userEJB.merge(user);
    }
}
