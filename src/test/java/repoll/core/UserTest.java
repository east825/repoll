package repoll.core;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.After;
import org.junit.Test;
import repoll.mappers.MapperException;
import repoll.mappers.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserTest extends DatabaseTest {

    @After
    public void clearDatabase() {
        clearTestDatabase();
    }

    @Test
    public void testUserCreation() throws MapperException, SQLException {
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
            assertNull(UserMapper.getInstance().loadById(testUser.getId()));
        }
    }

    @Test(expected = MapperException.class)
    public void testConflictLogin() throws MapperException {
        User u1 = User.builder("login", "foo").build();
        User u2 = User.builder("login", "bar").build();
        u1.insert();
        u2.insert();
    }

    @Test
    public void testInvalidParameters() throws MapperException {
        try {
            User.builder(null, "passwd").build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User.builder("login", null).build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User u = User.builder("login", "passwd").build();
            u.setLogin(null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User u = User.builder("login", "passwd").build();
            u.setPassword(null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User.builder("login", "passwd").additionalInfo(null).build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User.builder("login", "passwd").registrationDate(null).build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User.builder("login", "passwd").lastVisitDate(null).build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User.builder("login", "passwd").firstName(null).build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User.builder("login", "passwd").middleName(null).build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            User.builder("login", "passwd").lastName(null).build();
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test(expected = AssertionError.class)
    public void idModificationOutsideMapper() throws MapperException {
        User u = User.builder("foo", "bar").build();
        u.insert();
        u.setId(42);
    }

    @Test(expected = MapperException.class)
    public void userNotInsertedBeforeUpdate() throws MapperException {
        User.builder("login", "passwd").build().update();
    }

    @Test(expected = MapperException.class)
    public void userNotInsertedBeforeDelete() throws MapperException {
        User.builder("login", "passwd").build().delete();
    }

    @Test(expected = MapperException.class)
    public void userInsertedTwice() throws MapperException {
        User u = User.builder("login", "passwd").build();
        u.insert();
        u.insert();
    }


}
