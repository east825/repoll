package repoll.server;

import org.junit.Ignore;
import org.junit.Test;
import repoll.models.Answer;
import repoll.models.Poll;
import repoll.models.User;
import repoll.server.mappers.Facade;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;
import repoll.server.mappers.UserMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest extends DatabaseTest {
    private static final String SELECT_ALL_FIELDS_QUERY = "select * from \"User\" where id = ?";

    @Test
    public void insertAndDeleteUser() throws MapperException, SQLException {
        User user = User.builder("someLogin", "somePassword").additionalInfo("It's just test user").build();
        Mappers.insert(user);
        assertTrue(user.isSaved());
        try (PreparedStatement statement = testConnection.prepareStatement("select * from \"User\" where id = ?")) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertSame(user, Mappers.loadById(User.class, user.getId()));
            Mappers.delete(user);
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
        Mappers.insert(user);
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
            Mappers.update(user);

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
        Mappers.insert(u1);
        Mappers.insert(u2);
    }

    @Ignore
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
        Mappers.insert(u);
        u.setId(42);
    }

    @Test(expected = MapperException.class)
    public void userNotInsertedBeforeUpdate() throws MapperException {
        Mappers.update(User.newFromCredentials("login", "passwd"));
    }

    @Test(expected = MapperException.class)
    public void userNotInsertedBeforeDelete() throws MapperException {
        Mappers.delete(User.newFromCredentials("login", "passwd"));
    }

    @Test(expected = MapperException.class)
    public void userInsertedTwice() throws MapperException {
        User u = User.builder("login", "passwd").build();
        Mappers.insert(u);
        Mappers.insert(u);
    }

    @Test
    public void authoredPolls() throws MapperException {
        User user1 = Mappers.insert(User.newFromCredentials("login1", "passwd"));
        User user2 = Mappers.insert(User.newFromCredentials("login2", "passwd"));
        User user3 = Mappers.insert(User.newFromCredentials("login3", "passwd"));
        Facade.Users.createPoll(user1, "poll #1");
        Facade.Users.createPoll(user1, "poll #2");
        Facade.Users.createPoll(user2, "poll #3");
        assertEquals(2, Facade.Users.getAuthoredPolls(user1).size());
        assertEquals(1, Facade.Users.getAuthoredPolls(user2).size());
        assertEquals(0, Facade.Users.getAuthoredPolls(user3).size());
    }

    @Test
    public void authoredCommentaries() throws MapperException {
        User user1 = Mappers.insert(User.newFromCredentials("login1", "passwd"));
        User user2 = Mappers.insert(User.newFromCredentials("login2", "passwd"));
        Poll poll = Facade.Users.createPoll(user1, "title");
        Facade.Users.commentPoll(user1, poll, "commentary #1");
        Facade.Users.commentPoll(user1, poll, "commentary #2");
        Facade.Users.commentPoll(user2, poll, "commentary #3");
        assertEquals(2, Facade.Users.getCommentaries(user1).size());
        assertEquals(1, Facade.Users.getCommentaries(user2).size());
    }

    @Test
    public void authoredVotes() throws MapperException {
        User user1 = Mappers.insert(User.newFromCredentials("login1", "passwd"));
        User user2 = Mappers.insert(User.newFromCredentials("login2", "passwd"));
        Poll poll = Facade.Users.createPoll(user1, "title");
        Answer answer1 = Facade.Polls.addAnswer(poll, "answer #1");
        Answer answer2 = Facade.Polls.addAnswer(poll, "answer #2");
        Facade.Users.vote(user1, answer1);
        Facade.Users.vote(user1, answer2);
        Facade.Users.vote(user2, answer1);
        assertEquals(2, Facade.Users.getVotes(user1).size());
        assertEquals(1, Facade.Users.getVotes(user2).size());
    }

    @Ignore
    @Test
    public void userCanVote() throws MapperException {
        User user = User.newFromCredentials("login", "password");
        Poll poll = Facade.Users.createPoll(user, "New poll");
        List<Answer> answers = Facade.Polls.addAnswers(poll, "answer #1", "answer #2");
        assertTrue(Facade.Users.canVoteIn(user, poll));
        Facade.Users.vote(user, answers.get(0));
        assertFalse(Facade.Users.canVoteIn(user, poll));
    }
}
