package repoll.server;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import repoll.TestUtil;
import repoll.util.LoggingUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class DatabaseTest {

    private static final Logger LOG = Logger.getLogger(DatabaseTest.class);
    static {
        LoggingUtil.configure();
    }

    protected static Connection testConnection;

    @BeforeClass
    public static void connectTestDatabase() {
        testConnection = TestUtil.initializeTestDatabaseConnection();
    }

    @AfterClass
    public static void closeConnection() {
       TestUtil.closeConnection(testConnection);
    }

    @Before
    public void clearDatabaseBefore() {
        TestUtil.clearDatabase(testConnection);
    }

    @After
    public void clearDatabaseAfter() {
        TestUtil.clearDatabase(testConnection);
    }

    protected void executeCountQueryAndCheckResult(String query, int expected) throws SQLException {
        try (Statement statement = testConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(resultSet.next());
            assertEquals(expected, resultSet.getLong(1));
        }
    }
}
