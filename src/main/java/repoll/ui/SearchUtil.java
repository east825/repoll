package repoll.ui;

import repoll.core.ConnectionProvider;
import repoll.core.Poll;
import repoll.core.User;
import repoll.mappers.MapperException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SearchUtil {
    private static final Logger LOG = Logger.getLogger(SearchUtil.class.getName());
    public static final Connection CONNECTION = ConnectionProvider.connection();

    public static List<Poll> findPolls(String query) {
        List<Poll> results = new ArrayList<>();
        String[] words = query.split("\\s+");
        try {
        forAllPolls:
            for (Poll poll : Poll.getMapper().all()) {
                for (String word : words) {
                    if (!(poll.getTitle().contains(word)) || poll.getDescription().contains(word)) {
                        continue forAllPolls;
                    }
                }
                results.add(poll);
            }
        } catch (MapperException e) {
            LOG.throwing("SearchUtil", "findPolls", e);
        }
        return results;
    }

    public static User findUser(String login) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("select id from \"User\" where login = ?");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return User.getMapper().loadById(resultSet.getLong("id"));
        } catch (SQLException|MapperException e) {
            LOG.throwing("SearchUtil", "findUser", e);
            return null;
        }
    }

//    select count(V.id) from "Vote" V join "Answer" A on V.answer_id = A.id where V.user_id = 1 and A.poll_id = 8;

}
