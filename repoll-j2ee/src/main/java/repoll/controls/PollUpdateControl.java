package repoll.controls;

import repoll.beans.PollEJB;
import repoll.entities.Poll;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * @author Mikhail Golubev
 */
@Named
@ViewScoped
public class PollUpdateControl {

    @EJB
    private PollEJB pollEJB;

    private Poll poll;

    @PostConstruct
    private void initialize() {
        poll = new Poll();
    }

    public void findPollById() {
        poll = pollEJB.findById(poll.getId());
        if (poll == null) {
            ControlUtil.send404Error();
        }
    }

    public String update() {
        pollEJB.merge(poll);
        return "/polls/view.xhtml?id=" + poll.getId() + "&faces-redirect=true";
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
