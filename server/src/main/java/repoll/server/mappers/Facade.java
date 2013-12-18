package repoll.server.mappers;

import org.jetbrains.annotations.NotNull;
import repoll.models.*;

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
        public static List<Poll> getAuthoredPolls(@NotNull User user) throws MapperException {
            return Mappers.selectRelated(Poll.class, user);
        }

        @NotNull
        public static List<Vote> getVotes(@NotNull User user) throws MapperException {
            return Mappers.selectRelated(Vote.class, user);
        }

        public static List<Commentary> getCommentaries(@NotNull User user) throws MapperException {
            return Mappers.selectRelated(Commentary.class, user);
        }
    }

    public static final class Polls {
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
        public static List<Vote> getVotes(Answer answer) throws MapperException {
            return Mappers.selectRelated(Vote.class, answer);
        }
    }
}
