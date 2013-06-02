package repoll.core;

import org.junit.After;
import org.junit.Test;
import repoll.mappers.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;

public class TestUser extends DatabaseTest {

    @After
    public void clearDatabase() {
        clearTestDatabase();
    }

    @Test
    public void testUserCreation() throws Exception {
        User testUser = User.builder("someLogin", "somePassword").additionalInfo("It's just test user").build();
        testUser.insert();
        Connection connection = ConnectionProvider.connection();
        try (PreparedStatement statement = connection.prepareStatement("select * from \"User\" where id = ?")) {
            statement.setLong(1, testUser.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue("No record with specified id in User table", resultSet.next());
            assertSame("User instance not cached in identity map", testUser, UserMapper.getInstance().loadById(testUser.getId()));
            testUser.delete();
            resultSet = statement.executeQuery();
            assertFalse("Record with specified id was not deleted", resultSet.next());
        }

    }
}
