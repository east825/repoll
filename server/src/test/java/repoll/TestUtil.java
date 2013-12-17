package repoll;

import repoll.models.*;
import repoll.server.Statements;
import repoll.server.mappers.MapperException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtil {

    public static Poll newAnonymousPoll(String title) throws MapperException {
        return newAnonymousPoll(title, "");
    }

    public static Poll newAnonymousPoll(String title, String description) throws MapperException {
        Poll poll = new Poll(null, title, description);
        poll.insert();
        return poll;
    }

    public static Commentary newAnonymousCommentary(Poll poll, String message) throws MapperException {
        Commentary commentary = new Commentary(null, poll, message);
        commentary.insert();
        return commentary;
    }

    public static Connection initializeTestDatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:db/test;create=true");
            ConnectionProvider.registerConnection(connection);
            return connection;
        } catch (SQLException e) {
            throw new AssertionError("Error while connecting to test database", e);
        }
    }

    public static void createDatabaseSchema(Connection connection) {
        try {
            executeUpdateStatements(connection, Statements.tableCreationStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating test database schema", e);
        }
    }

    public static void dropDatabaseSchema(Connection connection) {
        try {
            executeUpdateStatements(connection, Statements.tableRemovalStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while removing test database schema", e);
        }
    }

    public static void clearDatabase(Connection connection) {
        try {
            if (!databaseIsEmpty()) {
                executeUpdateStatements(connection, Statements.tableCleaningStatements());
            }
        } catch (SQLException e) {
            throw new AssertionError("Error while clearing test database content", e);
        }
    }

    private static boolean databaseIsEmpty() {
        return false;
    }

    public static void executeUpdateStatements(Connection connection, Iterable<String> statements) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            for (String s: statements) {
                statement.executeUpdate(s);
            }
        }
    }
}
