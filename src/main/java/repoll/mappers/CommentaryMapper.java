package repoll.mappers;

import repoll.core.Commentary;
import repoll.core.Poll;
import repoll.core.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CommentaryMapper extends AbstractMapper<Commentary> {
    private static final CommentaryMapper INSTANCE = new CommentaryMapper();

    public static CommentaryMapper getInstance() {
        return INSTANCE;
    }

    private CommentaryMapper() {}

    public static final String SEARCH_QUERY = "select * from \"Commentary\" where id = ?";
    public static final String UPDATE_QUERY = "update \"Commentary\" set " +
            "user_id = ?, poll_id = ?, message = ? where id = ?";

    public static final String INSERT_QUERY = "insert into \"Commentary\" " +
            "(user_id, poll_id, message) values (?, ?, ?)";
    public static final String DELETE_QUERY = "delete from \"Commentary\" where id = ?";

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
    protected PreparedStatement getUpdateStatement(Commentary commentary) throws SQLException {
        validate(commentary);
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        try {
            User author = commentary.getAuthor();
            if (author != null) {
                statement.setLong(1, author.getId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setLong(2, commentary.getPoll().getId());
            statement.setString(3, commentary.getMessage());
            statement.setLong(4, commentary.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getInsertStatement(Commentary commentary) throws SQLException {
        validate(commentary);
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        try {
            User author = commentary.getAuthor();
            if (author != null) {
                statement.setLong(1, author.getId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setLong(2, commentary.getPoll().getId());
            statement.setString(3, commentary.getMessage());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getDeleteStatement(Commentary domainObject) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        try {
            statement.setLong(1, domainObject.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected Commentary loadObject(ResultSet resultSet) throws SQLException, MapperException {
        return new Commentary(
                Mappers.getForClass(User.class).loadById(resultSet.getLong("user_id")),
                Mappers.getForClass(Poll.class).loadById(resultSet.getLong("poll_id")),
                resultSet.getString("message")
        );
    }

    private void validate(Commentary commentary) {
        if (commentary.getPoll() == null) {
            throw new NullPointerException("Poll of " + commentary + " is undefined");
        } else if (!commentary.getPoll().isSaved()) {
            throw new IllegalStateException("Poll of " + commentary + " should be saved first");
        }
        if (commentary.getAuthor() != null && !commentary.getAuthor().isSaved()) {
            throw new IllegalStateException("Author of " + commentary + " should be saved first");
        }
    }
}
