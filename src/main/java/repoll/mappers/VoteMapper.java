package repoll.mappers;

import repoll.core.Answer;
import repoll.core.User;
import repoll.core.Vote;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper extends AbstractMapper<Vote> {
    private static VoteMapper INSTANCE = new VoteMapper();

    public static VoteMapper getInstance() {
        return INSTANCE;
    }

    public static final String SEARCH_QUERY = "select * from \"Vote\" where id = ?";
    public static final String UPDATE_QUERY = "update \"Vote\" set user_id = ?, answer_id = ? where id = ?";
    public static final String INSERT_QUERY = "insert into \"Vote\" (user_id, answer_id) values (?, ?)";
    public static final String DELETE_QUERY = "delete from \"Vote\" where id = ?";

    private VoteMapper() {
    }

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
            statement.setLong(1, vote.getAuthor().getId());
            statement.setLong(2, vote.getAnswer().getId());
            statement.setLong(3, vote.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getInsertStatement(Vote vote) throws SQLException {
        validate(vote);
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
        try {
            statement.setLong(1, vote.getAuthor().getId());
            statement.setLong(2, vote.getAnswer().getId());
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
    protected Vote loadObject(ResultSet vote) throws SQLException, MapperException {
        return new Vote(
                Mappers.getForClass(User.class).loadById(vote.getLong("user_id")),
                Mappers.getForClass(Answer.class).loadById(vote.getLong("answer_id"))
        );
    }

    private void validate(Vote vote) {
        if (vote.getAnswer() == null) {
            throw new NullPointerException("Answer of " + vote + " is undefined");
        } else if (!vote.getAnswer().isSaved()) {
            throw new IllegalStateException("Answer of " + vote + " should be saved first");
        }
        if (vote.getAuthor() != null && !vote.getAuthor().isSaved()) {
            throw new IllegalStateException("Author of " + vote + " should be saved first");
        }

    }
}
