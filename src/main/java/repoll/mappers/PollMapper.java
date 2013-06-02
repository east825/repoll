package repoll.mappers;

import repoll.core.Poll;
import repoll.core.User;

import java.sql.*;

public class PollMapper extends AbstractMapper<Poll> {
    private static final PollMapper INSTANCE = new PollMapper();

    public static PollMapper getInstance() {
        return INSTANCE;
    }

    public static final String SEARCH_QUERY = "select * from \"Poll\" where id = ?";
    public static final String UPDATE_QUERY = "update \"Poll\" set " +
            "user_id = ?, title = ?, description = ?, creation_datetime = ?, where id = ?";

    public static final String INSERT_QUERY = "insert into \"Poll\" " +
            "(user_id, title, description, creation_datetime) values (?, ?, ?, ?)";
    public static final String DELETE_QUERY = "delete from \"Poll\" where id = ?";

    private PollMapper() {}

    @Override
    protected PreparedStatement getLoadByIdStatement(long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SEARCH_QUERY);
        try {
            statement.setLong(1, id);
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getUpdateStatement(Poll poll) throws SQLException {
        validate(poll);
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        try {
            User author = poll.getAuthor();
            if (author != null) {
                statement.setLong(1, author.getId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setString(2, poll.getTitle());
            statement.setString(3, poll.getDescription());
            statement.setTimestamp(4, Util.dateToTimestamp(poll.getCreationDate()));
            statement.setLong(5, poll.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getInsertStatement(Poll poll) throws SQLException {
        validate(poll);
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        try {
            User author = poll.getAuthor();
            if (author != null) {
                statement.setLong(1, author.getId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setString(2, poll.getTitle());
            statement.setString(3, poll.getDescription());
            statement.setTimestamp(4, Util.dateToTimestamp(poll.getCreationDate()));
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getDeleteStatement(Poll poll) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        try {
            statement.setLong(1, poll.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected Poll loadObject(ResultSet resultSet) throws SQLException, MapperException {
        Poll poll = new Poll(
                Mappers.getForClass(User.class).loadById(resultSet.getLong("user_id")),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getTimestamp("creation_datetime")
        );
        poll.setId(resultSet.getLong("id"));
        return poll;
    }

    private void validate(Poll poll) {
        User author = poll.getAuthor();
        if (author != null && !author.isSaved()) {
            throw new IllegalStateException("Author of " + poll + " should be saved first");
        }
    }
}
