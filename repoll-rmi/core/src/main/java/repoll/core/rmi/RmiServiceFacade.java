package repoll.core.rmi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.models.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public interface RmiServiceFacade extends Remote {
    String SERVICE_URL = "//localhost/RmiServiceFacade";

    /*
     * Common actions
     */
    @NotNull
    <T extends DomainObject> T save(@NotNull T object) throws RemoteException;

    <T extends DomainObject> void delete(@NotNull T object) throws RemoteException;


    /*
     * User actions
     */

    @Nullable
    User findUser(@NotNull String login, @NotNull String password) throws RemoteException;

    @NotNull
    User createUser(@NotNull String login, @NotNull String password) throws RemoteException;

    @NotNull
    List<Poll> getUserPolls(@NotNull User user) throws RemoteException;

    @NotNull
    List<Vote> getUserVotes(@NotNull User user) throws RemoteException;

    @NotNull
    List<Commentary> getUserCommentaries(@NotNull User user) throws RemoteException;

    boolean userCanVoteIn(@NotNull User user, @NotNull Poll poll) throws RemoteException;

    void vote(@NotNull User user, @NotNull Answer answer) throws RemoteException;

    void comment(@NotNull User user, @NotNull Poll poll, @NotNull String message) throws RemoteException;

    /*
     * Poll actions
     */

    List<Poll> findPolls(@NotNull String query) throws RemoteException;

    @NotNull
    Poll createPoll(@NotNull User author, @NotNull String title, @NotNull String description) throws RemoteException;

    @NotNull
    List<Commentary> getPollCommentaries(@NotNull Poll poll) throws RemoteException;

    @NotNull
    List<Answer> getPollAnswers(@NotNull Poll poll) throws RemoteException;

    /*
     * Answer actions
     */

    @NotNull
    Answer addAnswerToPoll(@NotNull String description, @NotNull Poll poll) throws RemoteException;

    @NotNull
    List<Vote> getAnswerVotes(@NotNull Answer answer) throws RemoteException;


    /*
     * Commentary actions
     */

    @NotNull
    Commentary addCommentaryToPoll(@NotNull String message, @NotNull User author, @NotNull Poll poll) throws RemoteException;
}
