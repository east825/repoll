package repoll.util;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.models.Poll;
import repoll.models.User;
import repoll.server.mappers.ConnectionProvider;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchUtil {
    private static final Logger LOG = Logger.getLogger(SearchUtil.class);
    public static final Connection CONNECTION = ConnectionProvider.connection();
    private static final String COUNT_VOTES_QUERY = "select count(V.id) from \"Vote\" V join \"Answer\" A on V.answer_id = A.id where V.user_id = ? and A.poll_id = ?";

    /**
     * Service class
     */
    private SearchUtil() {
        // empty
    }

    @NotNull
    public static List<Poll> findPolls(@NotNull String query) {
        List<Poll> results = new ArrayList<>();
        String[] words = query.split("\\s+");
        try {
        overPolls:
            for (Poll poll : Mappers.getForClass(Poll.class).all()) {
                for (String word : words) {
                    String title = poll.getTitle().toLowerCase();
                    String description = poll.getDescription().toLowerCase();
                    word = word.toLowerCase();
                    if (!(title.contains(word) || description.contains(word))) {
                        continue overPolls;
                    }
                }
                results.add(poll);
            }
        } catch (MapperException e) {
            LOG.error(e);
        }
        return results;
    }

    @Nullable
    public static User findUserByLogin(@NotNull String login) {
        try {
            try (PreparedStatement statement = CONNECTION.prepareStatement("select id from \"User\" where login = ?")) {
                statement.setString(1, login);
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    return null;
                }
                return Mappers.getForClass(User.class).loadById(resultSet.getLong("id"));
            }
        } catch (SQLException|MapperException e) {
            LOG.error(e);
            return null;
        }
    }

    public static boolean userVotedInPoll(User user, Poll poll) {
        if (!user.isSaved() || !poll.isSaved()) {
            // maybe IllegalArgumentException is more appropriate
            return false;
        }
        try {
            try (PreparedStatement statement = CONNECTION.prepareStatement(COUNT_VOTES_QUERY)) {
                statement.setLong(1, user.getId());
                statement.setLong(2, poll.getId());
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    throw new AssertionError("Count aggregate query returned no data");
                }
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOG.error(e);
            return false;
        }
    }
}
