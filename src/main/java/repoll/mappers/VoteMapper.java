package repoll.mappers;

import repoll.core.Answer;
import repoll.core.DomainObject;
import repoll.core.User;
import repoll.core.Vote;

import java.sql.*;

public class VoteMapper extends AbstractMapper<Vote> {
    private static VoteMapper INSTANCE = new VoteMapper();

    public static VoteMapper getInstance() {
        return INSTANCE;
    }

    public static final String SEARCH_QUERY = "select * from \"Vote\" where id = ?";
    public static final String UPDATE_QUERY = "update \"Vote\" " +
            "set user_id = ?, answer_id = ?, creation_datetime = ? where id = ?";
    public static final String INSERT_QUERY = "insert into \"Vote\" " +
            "(user_id, answer_id, creation_datetime) values (?, ?, ?)";
    public static final String DELETE_QUERY = "delete from \"Vote\" where id = ?";
    private static final String SELECT_BY_USER_QUERY = "select id from \"Vote\" where user_id = ?";
    private static final String SELECT_BY_ANSWER_QUERY = "select id from \"Vote\" where answer_id = ?";
    private static final String SELECT_ALL_QUERY = "select id from \"Vote\"";

    private VoteMapper() {}

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
    protected PreparedStatement getUpdateStatement(Vote vote) throws SQLException {
        validate(vote);
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        try {
            User author = vote.getAuthor();
            if (author == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setLong(1, author.getId());
            }
            statement.setLong(2, vote.getAnswer().getId());
            statement.setTimestamp(3, Util.dateToTimestamp(vote.getCreationDate()));
            statement.setLong(4, vote.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getInsertStatement(Vote vote) throws SQLException {
        validate(vote);
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        try {
            User author = vote.getAuthor();
            if (author == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setLong(1, author.getId());
            }
            statement.setLong(2, vote.getAnswer().getId());
            statement.setTimestamp(3, Util.dateToTimestamp(vote.getCreationDate()));
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getDeleteStatement(Vote vote) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        try {
            statement.setLong(1, vote.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected Vote loadObject(ResultSet resultSet) throws SQLException, MapperException {
        Vote vote = new Vote(
                Mappers.getForClass(User.class).loadById(resultSet.getLong("user_id")),
                Mappers.getForClass(Answer.class).loadById(resultSet.getLong("answer_id")),
                resultSet.getTimestamp("creation_datetime")
        );
        vote.setId(resultSet.getLong("id"));
        return vote;
    }

    @Override
    protected PreparedStatement getSelectByStatement(DomainObject object) throws SQLException {
        PreparedStatement statement;
        if (object instanceof User) {
            statement = connection.prepareStatement(SELECT_BY_USER_QUERY);
        } else if (object instanceof Answer) {
            statement = connection.prepareStatement(SELECT_BY_ANSWER_QUERY);
        } else {
            return null;
        }
        try {
            statement.setLong(1, object.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getSelectAllStatement() throws SQLException {
        return connection.prepareStatement(SELECT_ALL_QUERY);
    }

    private void validate(Vote vote) {
        if (!vote.getAnswer().isSaved()) {
            throw new IllegalStateException("Answer of " + vote + " should be saved first");
        }
        if (vote.getAuthor() != null && !vote.getAuthor().isSaved()) {
            throw new IllegalStateException("Author of " + vote + " should be saved first");
        }

    }
}
