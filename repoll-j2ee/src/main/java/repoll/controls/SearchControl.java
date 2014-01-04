package repoll.controls;

import org.jetbrains.annotations.NotNull;
import repoll.beans.PollEJB;
import repoll.entities.Poll;
import repoll.entities.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Named
@SessionScoped
public class SearchControl implements Serializable {
    // view parameter
    private String query = "";

    @EJB
    PollEJB pollEJB;

    @Inject
    LoginControl loginControl;

    @NotNull
    public List<Poll> searchPolls() {
        return searchPolls(query);
    }

    @NotNull
    public List<Poll> searchPolls(@NotNull String query) {
        User currentUser = loginControl.getCurrentUser();
        if (query.isEmpty() && currentUser != null) {
            return currentUser.getPolls();
        } else {
            return pollEJB.findByTitleFuzzy(query);
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
