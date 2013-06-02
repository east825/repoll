package repoll.mappers;

import repoll.core.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserMapper extends AbstractMapper<User> {
    private static UserMapper INSTANCE = new UserMapper();

    public static UserMapper getInstance() {
        return INSTANCE;
    }

    public static final String SEARCH_QUERY = "select * from \"User\" where id = ?";
    public static final String UPDATE_QUERY = "update \"User\" set " +
            "login = ?, " +
            "password = ?, " +
            "first_name = ?, " +
            "middle_name = ?, " +
            "additional_info = ?, " +
            "registration_datetime = ?, " +
            "last_visit_datetime = ?" +
            "where id = ?";

    public static final String INSERT_QUERY = "insert into \"User\" " +
            "(login, password, first_name, last_name, middle_name, " +
            "additional_info, registration_datetime, last_visit_datetime)" +
            "values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_QUERY = "delete from \"User\" where id = ?";

    /*
     * Prevents external instantiation
     */
    private UserMapper() {
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
    protected PreparedStatement getUpdateStatement(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        try {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getMiddleName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getAdditionalInfo());
            statement.setTimestamp(7, Util.dateToTimestamp(user.getRegistrationDate()));
            statement.setTimestamp(8, Util.dateToTimestamp(user.getLastVisitDate()));
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getInsertStatement(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        try {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getMiddleName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getAdditionalInfo());
            statement.setTimestamp(7, Util.dateToTimestamp(user.getRegistrationDate()));
            statement.setTimestamp(8, Util.dateToTimestamp(user.getLastVisitDate()));
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected PreparedStatement getDeleteStatement(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        try {
            statement.setLong(1, user.getId());
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    @Override
    protected User loadObject(ResultSet resultSet) throws SQLException {
        User user = User.builder(resultSet.getString("login"), resultSet.getString("password"))
                .firstName(resultSet.getString("first_name"))
                .middleName(resultSet.getString("middle_name"))
                .lastName(resultSet.getString("last_name"))
                .additionalInfo(resultSet.getString("additional_info"))
                .registrationDate(resultSet.getTimestamp("registration_datetime"))
                .lastVisitDate(resultSet.getTimestamp("last_visit_datetime"))
                .build();
        user.setId(resultSet.getLong("id"));
        return user;
    }

}
