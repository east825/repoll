package repoll.core.rmi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.models.views.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public interface RmiServiceFacade extends Remote {
    String SERVICE_URL = "//localhost/RmiServiceFacade";

    /*
     * User actions
     */

    @Nullable
    UserView findUser(@NotNull String login, @NotNull String password) throws RemoteException;

    @NotNull
    UserView createUser(@NotNull String login, @NotNull String password) throws RemoteException;

    @NotNull
    List<PollView> getUserPolls(@NotNull UserView user) throws RemoteException;

    @NotNull
    List<VoteView> getUserVotes(@NotNull UserView user) throws RemoteException;

    @NotNull
    List<CommentaryView> getUserCommentaries(@NotNull UserView user) throws RemoteException;

    /*
     * Poll actions
     */

    List<PollView> findPolls(@NotNull String query) throws RemoteException;

    @NotNull
    PollView createPoll(@NotNull UserView author, @NotNull String title, @NotNull String description) throws RemoteException;

    @NotNull
    List<CommentaryView> getPollCommentaries(@NotNull PollView poll) throws RemoteException;

    @NotNull
    List<AnswerView> getPollAnswers(@NotNull PollView poll) throws RemoteException;

    /*
     * Answer actions
     */

    @NotNull
    AnswerView addAnswerToPoll(@NotNull String description, @NotNull PollView poll) throws RemoteException;

    @NotNull
    List<VoteView> getAnswerVotes(@NotNull AnswerView answer) throws RemoteException;


    /*
     * Commentary actions
     */

    @NotNull
    CommentaryView addCommentaryToPoll(@NotNull String message, @NotNull UserView author, @NotNull PollView poll) throws RemoteException;

    /*
     * Vote actions
     */

    @NotNull
    VoteView leftVote(@NotNull UserView author, @NotNull AnswerView answer) throws RemoteException;
}
