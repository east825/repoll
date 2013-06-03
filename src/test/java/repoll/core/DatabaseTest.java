package repoll.core;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {

    @BeforeClass
    public static void initializeTestDatabase() {
        connectTestDatabase();
//        createTestDatabaseSchema();
    }

//    @AfterClass
    public static void resetTestDatabase() {
        dropTestDatabaseSchema();
        try {
            ConnectionProvider.connection().close();
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
//            executeScript("scripts/create-schema.sql");
            executeUpdateStatements(Statements.tableCreationStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating test database schema", e);
        }
    }

    public static void dropTestDatabaseSchema() {
        try {
//            executeScript("scripts/drop-schema.sql");
            executeUpdateStatements(Statements.tableRemovalStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating test database schema", e);
        }
    }

    public static void clearTestDatabase() {
        try {
//            executeScript("scripts/clear-db.sql");
            executeUpdateStatements(Statements.tableCleaningStatements());
        } catch (SQLException e) {
            throw new AssertionError("Error while creating test database schema", e);
        }
    }

    private static void executeUpdateStatements(Iterable<String> statements) throws SQLException {
        Connection connection = ConnectionProvider.connection();
        try (Statement statement = connection.createStatement()) {
            for (String s: statements) {
                statement.executeUpdate(s);
            }
        }
    }

    private static void executeScript(String scriptName) throws SQLException, IOException{
        try (Connection connection = ConnectionProvider.connection()) {
            Statement statement = connection.createStatement();
            InputStreamReader resourceStream = new InputStreamReader(DatabaseTest.class.getResourceAsStream("/" + scriptName));
            BufferedReader bufferedReader = new BufferedReader(resourceStream);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            statement.executeUpdate(builder.toString());
        }
    }

    protected void executeCountQueryAndCheckResult(String query, int expected) throws SQLException {
        try (Statement statement = ConnectionProvider.connection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(resultSet.next());
            assertEquals(expected, resultSet.getLong(1));
        }
    }
}
