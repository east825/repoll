package repoll.server.rmi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.core.rmi.RmiServiceFacade;
import repoll.models.Answer;
import repoll.models.Commentary;
import repoll.models.Poll;
import repoll.models.User;
import repoll.models.views.*;
import repoll.server.mappers.MapperException;
import repoll.util.SearchUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public class RmiServiceFacadeImpl extends UnicastRemoteObject implements RmiServiceFacade {

    private static RmiServiceFacade INSTANCE = null;

    // dumb singleton
    public static synchronized RmiServiceFacade getInstance() throws RemoteException {
        if (INSTANCE == null) {
            INSTANCE = new RmiServiceFacadeImpl();
        }
        return INSTANCE;
    }

    private RmiServiceFacadeImpl() throws RemoteException {
        // empty
    }

    @Nullable
    @Override
    public UserView findUser(@NotNull String login, @NotNull String password) throws RemoteException {
        User user = SearchUtil.findUserByLogin(login);
        return user == null || !user.authenticate(password) ? null : user;
    }

    @NotNull
    @Override
    public UserView createUser(@NotNull String login, @NotNull String password) throws RemoteException {
        try {
            return User.newFromCredentials(login, password);
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<PollView> getUserPolls(@NotNull UserView user) throws RemoteException {
        try {
            return Collections.<PollView>unmodifiableList(getReal(user).getAuthoredPolls());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<VoteView> getUserVotes(@NotNull UserView user) throws RemoteException {
        try {
            return Collections.<VoteView>unmodifiableList(getReal(user).getVotes());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<CommentaryView> getUserCommentaries(@NotNull UserView user) throws RemoteException {
        try {
            return Collections.<CommentaryView>unmodifiableList(getReal(user).getCommentaries());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @Override
    public List<PollView> findPolls(@NotNull String query) throws RemoteException {
        return Collections.<PollView>unmodifiableList(SearchUtil.findPolls(query));
    }

    @NotNull
    @Override
    public PollView createPoll(@NotNull UserView author, @NotNull String title, @NotNull String description) throws RemoteException {
        Poll poll = new Poll(getReal(author), title, description);
        try {
            poll.insert();
        } catch (MapperException e) {
            throw wrap(e);
        }
        return poll;
    }

    @NotNull
    @Override
    public List<CommentaryView> getPollCommentaries(@NotNull PollView poll) throws RemoteException {
        try {
            return Collections.<CommentaryView>unmodifiableList(getReal(poll).getCommentaries());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<AnswerView> getPollAnswers(@NotNull PollView poll) throws RemoteException {
        try {
            return Collections.<AnswerView>unmodifiableList(getReal(poll).getAnswers());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public AnswerView addAnswerToPoll(@NotNull String description, @NotNull PollView poll) throws RemoteException {
        Answer answer = new Answer(getReal(poll), description);
        try {
            answer.insert();
        } catch (MapperException e) {
            throw wrap(e);
        }
        return answer;
    }

    @NotNull
    @Override
    public List<VoteView> getAnswerVotes(@NotNull AnswerView answer) throws RemoteException {
        try {
            return Collections.<VoteView>unmodifiableList(getReal(answer).getVotes());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public CommentaryView addCommentaryToPoll(@NotNull String message, @NotNull UserView author, @NotNull PollView poll) throws RemoteException {
        Commentary commentary = new Commentary(getReal(author), getReal(poll), message);
        try {
            commentary.insert();
        } catch (MapperException e) {
            throw wrap(e);
        }
        return commentary;
    }

    @NotNull
    @Override
    public VoteView leftVote(@NotNull UserView author, @NotNull AnswerView answer) throws RemoteException {
        try {
            return getReal(author).voteFor(getReal(answer));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    private static RemoteException wrap(Exception e) throws RemoteException {
        return new RemoteException("Internal server error", e);
    }

    private static Answer getReal(@NotNull AnswerView answerView) throws RemoteException {
        try {
            return Answer.getMapper().loadById(answerView.getId());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    private static User getReal(@NotNull UserView userView) throws RemoteException {
        try {
            return User.getMapper().loadById(userView.getId());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    private static Poll getReal(@NotNull PollView pollView) throws RemoteException {
        try {
            return Poll.getMapper().loadById(pollView.getId());
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    public static void main(String[] args) throws RemoteException {
        RmiServiceFacade facade = getInstance();
        System.out.println(facade.findPolls("bed"));
    }
}
