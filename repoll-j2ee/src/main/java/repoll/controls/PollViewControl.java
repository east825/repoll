package repoll.controls;

import org.jetbrains.annotations.NotNull;
import repoll.beans.*;
import repoll.entities.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Named
@ViewScoped
public class PollViewControl {

    private static final String[] COLORS = {"#69D2E7", "#A7DBD8", "#E0E4CC", "#F38630", "#FA6900"};
    private static final String BACKGROUND_COLOR = "#EEEEEE";

    @EJB
    private PollEJB pollEJB;
    @EJB
    private UserEJB userEJB;
    @EJB
    private CommentaryEJB commentaryEJB;
    @EJB
    private AnswerEJB answerEJB;
    @EJB
    private VoteEJB voteEJB;
    @Inject
    private LoginControl loginControl;

    private User currentUser;
    private Poll poll;
    private long selectedAnswerID;
    private String commentMessage;

    public void findPollById() {
        poll = pollEJB.findById(poll.getId());
//        if (poll == null) {
//            ControlUtil.redirect("/polls/list.xhtml");
//        }
        if (poll != null) {
            List<Answer> answers = poll.getAnswers();
            selectedAnswerID = answers.size() > 0 ? answers.get(0).getId() : 0;
        }
    }

    @PostConstruct
    private void initialize() {
        poll = new Poll();
        currentUser = loginControl.getCurrentUser();
    }

    /**
     * Check whether current user can vote in this poll.
     */
    public boolean showResults() {
        return currentUser == null || userEJB.userVotedInPoll(currentUser, poll);
    }

    /**
     * Leave vote by current user.
     */
    public String vote() {
        Answer selected = answerEJB.findById(selectedAnswerID);
        if (selected != null) {
            voteEJB.persist(new Vote(currentUser, selected));
            userEJB.merge(currentUser);
            answerEJB.merge(selected);
        }
        return "pollView";
    }

    /**
     * Leave comment by current user
     */
    public String comment() {
        commentaryEJB.persist(new Commentary(currentUser, poll, commentMessage));
        pollEJB.merge(poll);
        userEJB.merge(currentUser);
        return "pollView";
    }

    /**
     * Get poll results containing answer titles, vote counts and colors in chart.
     */
    public List<VotingResult> getResults() {
        List<VotingResult> results = new ArrayList<VotingResult>();
        boolean hasValue = false;
        List<Answer> answers = poll.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            int count = answer.getVotes().size();
            if (count != 0) {
                hasValue = true;
            }
            String title = answer.getDescription();
            String color = COLORS[i % COLORS.length];
            results.add(new VotingResult(title, color, count));
        }
        if (!hasValue) {
            results.add(new VotingResult("_background", BACKGROUND_COLOR, 1));
        }
        return results;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public long getSelectedAnswerID() {
        return selectedAnswerID;
    }

    public void setSelectedAnswerID(long selectedAnswerID) {
        this.selectedAnswerID = selectedAnswerID;
    }

    public static class VotingResult {
        private final String title;
        private final String color;
        private final int votesCount;

        public VotingResult(@NotNull String title, @NotNull String color, int votesCount) {
            this.title = title;
            this.color = color;
            this.votesCount = votesCount;
        }

        @NotNull
        public String getTitle() {
            return title;
        }

        @NotNull
        public String getColor() {
            return color;
        }

        public int getVotesCount() {
            return votesCount;
        }
    }
}
