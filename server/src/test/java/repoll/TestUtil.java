package repoll;

import repoll.models.Commentary;
import repoll.models.Poll;
import repoll.server.Statements;
import repoll.server.mappers.ConnectionProvider;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtil {

    private static final String TEST_DB_PATH = "db/test";

    public static Poll newAnonymousPoll(String title) throws MapperException {
        return newAnonymousPoll(title, "");
    }

    public static Poll newAnonymousPoll(String title, String description) throws MapperException {
        return Mappers.insert(new Poll(null, title, description));
    }

    public static Commentary newAnonymousCommentary(Poll poll, String message) throws MapperException {
        return Mappers.insert(new Commentary(null, poll, message));
    }

    public static Connection initializeTestDatabaseConnection() {
        ConnectionProvider.registerConnection(TEST_DB_PATH, true);
        return ConnectionProvider.connection();
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
