package repoll.mappers;

import repoll.core.DomainObject;
import repoll.core.Poll;
import repoll.core.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PollMapper extends AbstractMapper<Poll> {
    private static final PollMapper INSTANCE = new PollMapper();

    public static PollMapper getInstance() {
        return INSTANCE;
    }

    public static final String SEARCH_QUERY = "select * from \"Poll\" where id = ?";
    public static final String UPDATE_QUERY = "update \"Poll\" set " +
            "user_id = ?, title = ?, description = ?, creation = ?, where id = ?";

    public static final String INSERT_QUERY = "insert into \"Poll\" " +
            "(user_id, title, description, creation) values (?, ?, ?, ?)";
    public static final String DELETE_QUERY = "delete from \"Poll\" where id = ?";

    private final Map<Long, Poll> loadedMap = new HashMap<>();

    private PollMapper() {
        super();
    }

    @Override
    protected Map<Long, Poll> getLoadedMap() {
        return loadedMap;
    }

    @Override
    protected PreparedStatement getLoadByIdStatement(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SEARCH_QUERY)) {
            statement.setLong(1, id);
            return statement;
        }
    }

    @Override
    protected PreparedStatement getUpdateStatement(Poll poll) throws SQLException {
        checkAuthorField(poll.getAuthor());
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setLong(1, poll.getAuthor().getId());
            statement.setString(2, poll.getTitle());
            statement.setString(3, poll.getDescription());
            statement.setTimestamp(4, Util.dateToTimestamp(poll.getCreationDate()));
            statement.setLong(5, poll.getId());
            return statement;
        }
    }

    @Override
    protected PreparedStatement getInsertStatement(Poll poll) throws SQLException {
        checkAuthorField(poll.getAuthor());
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setLong(1, poll.getAuthor().getId());
            statement.setString(2, poll.getTitle());
            statement.setString(3, poll.getDescription());
            statement.setTimestamp(4, Util.dateToTimestamp(poll.getCreationDate()));
            return statement;
        }
    }

    @Override
    protected PreparedStatement getDeleteStatement(Poll poll) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, poll.getId());
            return statement;
        }
    }

    @Override
    protected Poll loadObject(ResultSet resultSet) throws SQLException, MapperException {
        long userId = resultSet.getLong("user_id");
        String title = resultSet.getString("title");
        return new Poll(UserMapper.getInstance().loadById(userId), title);
    }

    private void checkAuthorField(User author) {
        if (author.getId() == DomainObject.UNSAVED_OBJECT_ID) {
            throw new IllegalStateException("Author should be saved first");
        }
    }
}
