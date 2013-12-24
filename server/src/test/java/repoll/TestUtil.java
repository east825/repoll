package repoll;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.models.Commentary;
import repoll.models.Poll;
import repoll.server.Statements;
import repoll.server.mappers.ConnectionProvider;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;
import repoll.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtil {
    private static final Logger LOG = Logger.getLogger(TestUtil.class);

    private static final String TEST_DB_PATH = "db/test";

    /**
     * Service class
     */
    private TestUtil() {
        // empty
    }

    public static Poll newAnonymousPoll(String title) throws MapperException {
        return newAnonymousPoll(title, "");
    }

    public static Poll newAnonymousPoll(String title, String description) throws MapperException {
        return Mappers.insert(new Poll(null, title, description));
    }

    public static Commentary newAnonymousCommentary(Poll poll, String message) throws MapperException {
        return Mappers.insert(new Commentary(null, poll, message));
    }

    @NotNull
    public static Connection initializeTestDatabaseConnection() {
        ConnectionProvider.registerConnection(TEST_DB_PATH, true);
        return ConnectionProvider.connection();
    }

    public static void closeConnection(@Nullable Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new AssertionError("Error while closing connection " + DatabaseUtil.getUrl(connection));
        }

    }

    public static void createDatabaseSchema(@NotNull Connection connection) {
        try {
            LOG.debug("Creating schema for database " + DatabaseUtil.getUrl(connection));
            executeUpdateStatements(connection, Statements.tableCreationStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating schema for connection " + DatabaseUtil.getUrl(connection), e);
        }
    }

    public static void dropDatabaseSchema(@NotNull Connection connection) {
        try {
            LOG.debug("Removing schema for database " + DatabaseUtil.getUrl(connection));
            executeUpdateStatements(connection, Statements.tableRemovalStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while removing schema for connection " + DatabaseUtil.getUrl(connection), e);
        }
    }

    public static void clearDatabase(@NotNull Connection connection) {
        try {
            LOG.debug("Clearing database " + DatabaseUtil.getUrl(connection));
            executeUpdateStatements(connection, Statements.tableCleaningStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while clearing database for connection " + DatabaseUtil.getUrl(connection), e);
        }
    }

    private static void executeUpdateStatements(@NotNull Connection connection, @NotNull Iterable<String> statements) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            for (String s : statements) {
                statement.executeUpdate(s);
            }
        }
    }

}
