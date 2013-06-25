package repoll.core;

import org.junit.Test;
import repoll.core.DatabaseTest;
import repoll.mappers.MapperException;
import repoll.mappers.UserMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

public class UserTest extends DatabaseTest {
    private static final String SELECT_ALL_FIELDS_QUERY = "select * from \"User\" where id = ?";

    @Test
    public void insertAndDeleteUser() throws MapperException, SQLException {
        User user = User.builder("someLogin", "somePassword").additionalInfo("It's just test user").build();
        user.insert();
        assertTrue(user.isSaved());
        try (PreparedStatement statement = testConnection.prepareStatement("select * from \"User\" where id = ?")) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertSame(user, User.getMapper().loadById(user.getId()));
            user.delete();
            assertFalse(user.isSaved());
            resultSet = statement.executeQuery();
            assertFalse(resultSet.next());
            assertNull(UserMapper.getInstance().loadById(user.getId()));
        }
    }

    @Test
    public void updateUser() throws MapperException, SQLException {
        Date lastVisitDate1 = new Date(100);
        Date lastVisitDate2 = new Date(200);
        Date registrationDate = new Date(300);
        User user = User.builder("login1", "password1")
                .firstName("FirstName1")
                .middleName("MiddleName1")
                .lastName("LastName1")
                .additionalInfo("info1")
                .registrationDate(registrationDate)
                .lastVisitDate(lastVisitDate1)
                .stackoverflowId(123)
                .build();
        user.insert();
        try (PreparedStatement statement = testConnection.prepareStatement(SELECT_ALL_FIELDS_QUERY)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("login1", resultSet.getString("login"));
            assertEquals("password1", resultSet.getString("password"));
            assertEquals("FirstName1", resultSet.getString("first_name"));
            assertEquals("MiddleName1", resultSet.getString("middle_name"));
            assertEquals("LastName1", resultSet.getString("last_name"));
            assertEquals("info1", resultSet.getString("additional_info"));
            assertEquals(registrationDate, resultSet.getTimestamp("registration_datetime"));
            assertEquals(lastVisitDate1, resultSet.getTimestamp("last_visit_datetime"));
            assertEquals(123, resultSet.getInt("stackoverflow_id"));

            user.setLogin("login2");
            user.setPassword("password2");
            user.setFirstName("FirstName2");
            user.setMiddleName("MiddleName2");
            user.setLastName("LastName2");
            user.setAdditionalInfo("info2");
            user.setLastVisitDate(lastVisitDate2);
            user.setStackoverflowId(42);
            user.update();

            resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("login2", resultSet.getString("login"));
            assertEquals("password2", resultSet.getString("password"));
            assertEquals("FirstName2", resultSet.getString("first_name"));
            assertEquals("MiddleName2", resultSet.getString("middle_name"));
            assertEquals("LastName2", resultSet.getString("last_name"));
            assertEquals("info2", resultSet.getString("additional_info"));
            assertEquals(registrationDate, resultSet.getTimestamp("registration_datetime"));
            assertEquals(lastVisitDate2, resultSet.getTimestamp("last_visit_datetime"));
            assertEquals(42, resultSet.getInt("stackoverflow_id"));
        }
    }

    @Test(expected = MapperException.class)
    public void conflictingLogin() throws MapperException {
        User u1 = User.builder("login", "foo").build();
        User u2 = User.builder("login", "bar").build();
        u1.insert();
        u2.insert();
    }

    @Test
    public void illegalParameters() throws MapperException {
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
        try {
            User.builder("login", "passwd").stackoverflowId(-10).build();
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

    @Test
    public void authoredPolls() throws MapperException {
        User user1 = User.newFromCredentials("login1", "passwd");
        User user2 = User.newFromCredentials("login2", "passwd");
        User user3 = User.newFromCredentials("login3", "passwd");
        user1.createPoll("poll #1");
        user1.createPoll("poll #2");
        user2.createPoll("poll #3");
        assertEquals(2, user1.getAuthoredPolls().size());
        assertEquals(1, user2.getAuthoredPolls().size());
        assertEquals(0, user3.getAuthoredPolls().size());
    }

    @Test
    public void authoredCommentaries() throws MapperException {
        User user1 = User.newFromCredentials("login1", "passwd");
        User user2 = User.newFromCredentials("login2", "passwd");
        Poll poll = user1.createPoll("title");
        user1.commentPoll(poll, "commentary #1");
        user1.commentPoll(poll, "commentary #2");
        user2.commentPoll(poll, "commentary #3");
        assertEquals(2, user1.getCommentaries().size());
        assertEquals(1, user2.getCommentaries().size());
    }

    @Test
    public void authoredVotes() throws MapperException {
        User user1 = User.newFromCredentials("login1", "passwd");
        User user2 = User.newFromCredentials("login2", "passwd");
        Poll poll = user1.createPoll("title");
        Answer answer1 = poll.addAnswer("answer #1");
        Answer answer2 = poll.addAnswer("answer #2");
        user1.voteFor(answer1);
        user1.voteFor(answer2);
        user2.voteFor(answer1);
        assertEquals(2, user1.getVotes().size());
        assertEquals(1, user2.getVotes().size());
    }
}
