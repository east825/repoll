package repoll.server.rmi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.core.rmi.RmiServiceFacade;
import repoll.models.*;
import repoll.server.mappers.Facade;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;
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
    public User findUser(@NotNull String login, @NotNull String password) throws RemoteException {
        User user = SearchUtil.findUserByLogin(login);
        return user == null || !user.authenticate(password) ? null : user;
    }

    @NotNull
    @Override
    public User createUser(@NotNull String login, @NotNull String password) throws RemoteException {
        try {
            return Mappers.insert(User.newFromCredentials(login, password));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<Poll> getUserPolls(@NotNull User user) throws RemoteException {
        try {
            return Collections.unmodifiableList(Facade.Users.getAuthoredPolls(user));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<Vote> getUserVotes(@NotNull User user) throws RemoteException {
        try {
            return Collections.unmodifiableList(Facade.Users.getVotes(user));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<Commentary> getUserCommentaries(@NotNull User user) throws RemoteException {
        try {
            return Collections.unmodifiableList(Facade.Users.getCommentaries(user));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @Override
    public void vote(@NotNull User user, @NotNull Answer answer) throws RemoteException {
        try {
            Facade.Users.vote(user, answer);
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @Override
    public void comment(@NotNull User user, @NotNull Poll poll, @NotNull String message) throws RemoteException {
        try {
            Facade.Users.comment(user, poll, message);
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @Override
    public boolean userCanVoteIn(@NotNull User user, @NotNull Poll poll) throws RemoteException {
        return Facade.Users.canVoteIn(user, poll);
    }

    @Override
    public List<Poll> findPolls(@NotNull String query) throws RemoteException {
        return Collections.unmodifiableList(SearchUtil.findPolls(query));
    }

    @NotNull
    @Override
    public Poll createPoll(@NotNull User author, @NotNull String title, @NotNull String description) throws RemoteException {
        try {
            Mappers.insert(new Poll(author, title, description));
        } catch (MapperException e) {
            throw wrap(e);
        }
        return new Poll(author, title, description);
    }

    @NotNull
    @Override
    public List<Commentary> getPollCommentaries(@NotNull Poll poll) throws RemoteException {
        try {
            return Collections.unmodifiableList(Facade.Polls.getCommentaries(poll));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public List<Answer> getPollAnswers(@NotNull Poll poll) throws RemoteException {
        try {
            return Collections.unmodifiableList(Facade.Polls.getAnswers(poll));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public Answer addAnswerToPoll(@NotNull String description, @NotNull Poll poll) throws RemoteException {
        Answer answer = new Answer(poll, description);
        try {
            Mappers.insert(answer);
        } catch (MapperException e) {
            throw wrap(e);
        }
        return answer;
    }

    @NotNull
    @Override
    public List<Vote> getAnswerVotes(@NotNull Answer answer) throws RemoteException {
        try {
            return Collections.unmodifiableList(Facade.Answers.getVotes(answer));
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @NotNull
    @Override
    public Commentary addCommentaryToPoll(@NotNull String message, @NotNull User author, @NotNull Poll poll) throws RemoteException {
        Commentary commentary = new Commentary(author, poll, message);
        try {
            Mappers.insert(commentary);
        } catch (MapperException e) {
            throw wrap(e);
        }
        return commentary;
    }

    @NotNull
    @Override
    public <T extends DomainObject> T save(@NotNull T object) throws RemoteException {
        try {
            return Mappers.save(object);
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    @Override
    public <T extends DomainObject> void delete(@NotNull T object) throws RemoteException {
        try {
            Mappers.delete(object);
        } catch (MapperException e) {
            throw wrap(e);
        }
    }

    private static RemoteException wrap(Exception e) throws RemoteException {
        // MapperException is not accessible at client: so I can't use exception chaining
        return new RemoteException("Internal server error: " + e.getMessage());
    }
}
