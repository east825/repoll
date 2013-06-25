package repoll.core;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import repoll.TestUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {

    protected static Connection testConnection = TestUtil.initializeTestDatabaseConnection();

//    @BeforeClass
//    public static void createDatabaseSchema() {
//        TestUtil.createDatabaseSchema(testConnection);
//    }

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
