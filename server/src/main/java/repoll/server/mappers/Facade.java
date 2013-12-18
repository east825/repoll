package repoll.server.mappers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.models.*;
import repoll.util.SearchUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
public class Facade {
    /**
     * Service class
     */
    private Facade() {
        // empty
    }

    public static final class Users {

        @NotNull
        public static Poll createPoll(@NotNull User user, @NotNull String title) throws MapperException{
            return Mappers.insert(new Poll(user, title));
        }

        @NotNull
        public static Commentary commentPoll(@Nullable User user, @NotNull Poll poll, @NotNull String message) throws MapperException {
            return Mappers.insert(new Commentary(user, poll, message));
        }

        @NotNull
        public static Vote vote(@Nullable User user, @NotNull Answer answer) throws MapperException {
            return Mappers.insert(new Vote(user, answer));
        }

        public static boolean canVoteIn(@NotNull User user, @NotNull Poll poll) {
            return SearchUtil.userVotedInPoll(user, poll);
        }

        @NotNull
        public static List<Poll> getAuthoredPolls(@NotNull User user) throws MapperException {
            return Mappers.selectRelated(Poll.class, user);
        }

        @NotNull
        public static List<Vote> getVotes(@NotNull User user) throws MapperException {
            return Mappers.selectRelated(Vote.class, user);
        }

        @NotNull
        public static List<Commentary> getCommentaries(@NotNull User user) throws MapperException {
            return Mappers.selectRelated(Commentary.class, user);
        }
    }

    public static final class Polls {
        @NotNull
        public static Answer addAnswer(@NotNull Poll poll, @NotNull String description) throws MapperException {
            return Mappers.insert(new Answer(poll, description));
        }

        @NotNull
        public static List<Answer> addAnswers(@NotNull Poll poll, @NotNull String... descriptions) throws MapperException {
            List<Answer> answers = new ArrayList<>();
            for (String description : descriptions) {
                answers.add(addAnswer(poll, description));
            }
            return answers;
        }

        @NotNull
        public static List<Answer> getAnswers(@NotNull Poll poll) throws MapperException {
            return Mappers.selectRelated(Answer.class, poll);
        }

        @NotNull
        public static List<Commentary> getCommentaries(@NotNull Poll poll) throws MapperException {
            return Mappers.selectRelated(Commentary.class, poll);
        }
    }

    public static final class Answers {
        @NotNull
        public static List<Vote> getVotes(@NotNull Answer answer) throws MapperException {
            return Mappers.selectRelated(Vote.class, answer);
        }

        public static int getVotesNumber(@NotNull Answer answer) throws MapperException {
            return getVotes(answer).size();
        }
    }
}
