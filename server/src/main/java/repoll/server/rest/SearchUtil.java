package repoll.server.rest;

import repoll.models.ConnectionProvider;
import repoll.models.Poll;
import repoll.models.User;
import repoll.server.mappers.MapperException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SearchUtil {
    private static final Logger LOG = Logger.getLogger(SearchUtil.class.getName());
    public static final Connection CONNECTION = ConnectionProvider.connection();
    private static final String COUNT_VOTES_QUERY = "select count(V.id) from \"Vote\" V join \"Answer\" A on V.answer_id = A.id where V.user_id = ? and A.poll_id = ?";

    public static List<Poll> findPolls(String query) {
        List<Poll> results = new ArrayList<>();
        String[] words = query.split("\\s+");
        try {
        overPolls:
            for (Poll poll : Poll.getMapper().all()) {
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
            LOG.throwing("SearchUtil", "findPolls", e);
        }
        return results;
    }

    public static User findUserByLogin(String login) {
        try {
            try (PreparedStatement statement = CONNECTION.prepareStatement("select id from \"User\" where login = ?")) {
                statement.setString(1, login);
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    return null;
                }
                return User.getMapper().loadById(resultSet.getLong("id"));
            }
        } catch (SQLException|MapperException e) {
            LOG.throwing("SearchUtil", "findUserByLogin", e);
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
            LOG.throwing("SearchUtil", "userVotedInPoll", e);
            return false;
        }
    }
}
