package repoll.core;

import org.junit.After;
import org.junit.Before;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {

    protected static Connection testConnection = initializeTestConnection();

    private static Connection initializeTestConnection() {
        connectTestDatabase();
        return ConnectionProvider.connection();
    }


    @Before
    public void setUp() {
        clearTestDatabase();
    }

    @After
    public void tearDown() {
        clearTestDatabase();
    }

    public static void resetTestDatabase() {
        dropTestDatabaseSchema();
        try {
            testConnection.close();
        } catch (SQLException e) {
            throw new AssertionError("Error while closing connection to test database", e);
        }
    }

    public static void connectTestDatabase() {
        try {
            ConnectionProvider.registerConnection(DriverManager.getConnection("jdbc:derby:db/test;create=true"));
        } catch (SQLException e) {
            throw new AssertionError("Error while connecting to test database", e);
        }
    }

    public static void createTestDatabaseSchema() {
        try {
            executeUpdateStatements(Statements.tableCreationStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating test database schema", e);
        }
    }

    public static void dropTestDatabaseSchema() {
        try {
            executeUpdateStatements(Statements.tableRemovalStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating test database schema", e);
        }
    }

    public static void clearTestDatabase() {
        try {
            executeUpdateStatements(Statements.tableCleaningStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating test database schema", e);
        }
    }

    private static void executeUpdateStatements(Iterable<String> statements) throws SQLException {
        try (Statement statement = testConnection.createStatement()) {
            for (String s: statements) {
                statement.executeUpdate(s);
            }
        }
    }

    protected void executeCountQueryAndCheckResult(String query, int expected) throws SQLException {
        try (Statement statement = testConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(resultSet.next());
            assertEquals(expected, resultSet.getLong(1));
        }
    }
}
