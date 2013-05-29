package repoll.mappers;

import repoll.core.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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

    private Map<Long, User> loadedMap = new HashMap<>();

    /*
     * Prevents external instantiation
     */
    private UserMapper() {
        super();
    }

    @Override
    protected Map<Long, User> getLoadedMap() {
        return loadedMap;
    }

    @Override
    protected PreparedStatement getLoadByIdStatement(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from \"User\" where id = ?")) {
            statement.setLong(1, id);
            return statement;
        }
    }

    @Override
    protected PreparedStatement getUpdateStatement(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getMiddleName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getAdditionalInfo());
            statement.setTimestamp(7, user.getRegistrationDate());
            statement.setTimestamp(8, user.getLastVisitDate());
            return statement;
        }
    }

    @Override
    protected PreparedStatement getInsertStatement(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getMiddleName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getAdditionalInfo());
            statement.setTimestamp(7, user.getRegistrationDate());
            statement.setTimestamp(8, user.getLastVisitDate());
            return statement;
        }
    }

    @Override
    protected PreparedStatement getDeleteStatement(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, user.getId());
            return statement;
        }
    }

    @Override
    protected User loadObject(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPasswordHash(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setMiddleName(resultSet.getString("middle_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setAdditionalInfo(resultSet.getString("additional_info"));
        user.setRegistrationDate(resultSet.getTimestamp("registration_datetime"));
        user.setLastVisitDate(resultSet.getTimestamp("last_visit_datetime"));
        return user;
    }

}
