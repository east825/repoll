package repoll.controls;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import repoll.beans.AnswerEJB;
import repoll.beans.PollEJB;
import repoll.entities.Answer;
import repoll.entities.Poll;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.Set;

/**
 * @author Mikhail Golubev
 */
@Model
public class PollCreateControl {
    @EJB
    private PollEJB pollEJB;
    @EJB
    private AnswerEJB answerEJB;

    @Inject
    private LoginControl loginControl;

    private Poll poll = new Poll();
//    private List<String> answerDescriptions;
    private String answers;

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    /*public List<String> getAnswerDescriptions() {
        return answerDescriptions;
    }

    public void setAnswerDescriptions(List<String> answerDescriptions) {
        this.answerDescriptions = answerDescriptions;
    }*/

    public String create() {
        if (pollEJB.findByTitle(poll.getTitle()) != null) {
            // TODO: slugify/normalize poll
            return ControlUtil.reportError("title", "Poll with this title already exists");
        }
/*
        for (String description : answerDescriptions) {
            poll.getAnswers().add(new Answer(description));
        }
*/

        poll.setAuthor(loginControl.getCurrentUser());
        pollEJB.persist(poll);
        Set<String> descriptions = Sets.newHashSet(Splitter.on(";")
                .trimResults()
                .omitEmptyStrings()
                .split(answers));
        for (String description : descriptions) {
            answerEJB.persist(new Answer(poll, description));
        }
        return "/polls/view.xhtml?id=" + poll.getId() + "&faces-redirect=true";
    }
}
