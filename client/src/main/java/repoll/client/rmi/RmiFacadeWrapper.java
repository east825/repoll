package repoll.client.rmi;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.core.rmi.RmiServiceFacade;
import repoll.models.*;

import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public class RmiFacadeWrapper {
    private static final Logger LOG = Logger.getLogger(RmiFacadeWrapper.class);

    private final RmiServiceFacade facade;

    public RmiFacadeWrapper(RmiServiceFacade facade) {
        this.facade = facade;
    }

    @NotNull
    public <T extends DomainObject> T save(@NotNull T object) {
        try {
            return facade.save(object);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    public <T extends DomainObject> void delete(T comment) {
        try {
            facade.delete(comment);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @Nullable
    public User findUser(@NotNull String login, @NotNull String password) {
        try {
            return facade.findUser(login, password);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public User createUser(@NotNull String login, @NotNull String password) {
        try {
            return facade.createUser(login, password);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public List<Poll> getUserPolls(@NotNull User user) {
        try {
            return facade.getUserPolls(user);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public List<Vote> getUserVotes(@NotNull User user) {
        try {
            return facade.getUserVotes(user);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public List<Commentary> getUserCommentaries(@NotNull User user) {
        try {
            return facade.getUserCommentaries(user);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    public boolean canVoteIn(@NotNull User user, @NotNull Poll poll) {
        try {
            return facade.userCanVoteIn(user, poll);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    public void vote(@NotNull User user, @NotNull Answer answer) {
        try {
            facade.vote(user, answer);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    public void comment(@NotNull User user, @NotNull Poll poll, @NotNull String message) {
        try {
            facade.comment(user, poll, message);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    public List<Poll> findPolls(@NotNull String query) {
        try {
            return facade.findPolls(query);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public Poll createPoll(@NotNull User author, @NotNull String title, @NotNull String description) {
        try {
            return facade.createPoll(author, title, description);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public List<Commentary> getPollCommentaries(@NotNull Poll poll) {
        try {
            return facade.getPollCommentaries(poll);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public List<Answer> getPollAnswers(@NotNull Poll poll) {
        try {
            return facade.getPollAnswers(poll);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public Answer addAnswerToPoll(@NotNull String description, @NotNull Poll poll) {
        try {
            return facade.addAnswerToPoll(description, poll);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public List<Vote> getAnswerVotes(@NotNull Answer answer) {
        try {
            return facade.getAnswerVotes(answer);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    @NotNull
    public Commentary addCommentaryToPoll(@NotNull String message, @NotNull User author, @NotNull Poll poll) {
        try {
            return facade.addCommentaryToPoll(message, author, poll);
        } catch (RemoteException e) {
            throw propagate(e);
        }
    }

    private RuntimeException propagate(Exception e) {
        return new RuntimeException(e);
    }
}
