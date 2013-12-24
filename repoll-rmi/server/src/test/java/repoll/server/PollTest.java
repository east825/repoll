package repoll.server;

import org.junit.Ignore;
import org.junit.Test;
import repoll.TestUtil;
import repoll.models.Poll;
import repoll.models.User;
import repoll.server.mappers.Facade;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static org.junit.Assert.*;

public class PollTest extends DatabaseTest {
    private static final String SELECT_ALL_FIELDS_QUERY = "select * from \"Poll\" where id = ?";

    @Test
    public void insertAndDeletePoll() throws MapperException, SQLException {
        User author = Mappers.insert(User.newFromCredentials("login", "passwd"));
        Poll poll = Mappers.insert(new Poll(author, "title"));
        assertTrue(poll.isSaved());
        Statement statement = testConnection.createStatement();
        assertTrue(statement.executeQuery("select * from \"User\"").next());
        assertTrue(statement.executeQuery("select * from \"Poll\"").next());
        Mappers.delete(author);
        Mappers.delete(poll);
        assertFalse(poll.isSaved());
        assertFalse(statement.executeQuery("select * from \"User\"").next());
        assertFalse(statement.executeQuery("select * from \"Poll\"").next());
    }

    @Test
    public void updatePoll() throws MapperException, SQLException {
        Date creationDate = new Date(100);
        Poll poll = Mappers.insert(new Poll(null, "title1", "description1", creationDate));
        try (PreparedStatement statement = testConnection.prepareStatement(SELECT_ALL_FIELDS_QUERY)) {
            statement.setLong(1, poll.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("title1", resultSet.getString("title"));
            assertEquals("description1", resultSet.getString("description"));
            assertEquals(creationDate, resultSet.getTimestamp("creation_datetime"));

            poll.setTitle("title2");
            poll.setDescription("description2");
            Mappers.update(poll);

            resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("title2", resultSet.getString("title"));
            assertEquals("description2", resultSet.getString("description"));
            assertEquals(creationDate, resultSet.getTimestamp("creation_datetime"));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void authorNotInsertedBeforePoll() throws MapperException {
        // Not inserted
        User author = User.builder("login", "passwd").build();
        Mappers.insert(new Poll(author, "title"));
    }

    @Test(expected = MapperException.class)
    public void conflictingTitle() throws MapperException {
        Mappers.insert(new Poll(null, "title"));
        Mappers.insert(new Poll(null, "title"));
    }

    @SuppressWarnings("ConstantConditions")
    @Ignore
    @Test
    public void illegalParameters() {
        User u1 = User.builder("login", "passwd").build();
        try {
            new Poll(u1, null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            new Poll(u1, "title", null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            new Poll(u1, "title", "description", null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            Poll p = new Poll(u1, "title");
            p.setDescription(null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            Poll p = new Poll(u1, "title");
            p.setTitle(null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }

        try {
            Poll p = new Poll(u1, "title");
            p.setDescription(null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void selectCommentaries() throws MapperException {
        Poll poll1 = TestUtil.newAnonymousPoll("titlel");
        Poll poll2 = TestUtil.newAnonymousPoll("title2");
        TestUtil.newAnonymousCommentary(poll1, "commentary #1");
        TestUtil.newAnonymousCommentary(poll1, "commentary #2");
        TestUtil.newAnonymousCommentary(poll2, "commentary #3");
        assertEquals(2, Facade.Polls.getCommentaries(poll1).size());
        assertEquals(1, Facade.Polls.getCommentaries(poll2).size());
    }

    @Test
    public void selectAnswers() throws MapperException {
        Poll poll1 = TestUtil.newAnonymousPoll("title1");
        Facade.Polls.addAnswers(poll1, "answer #1", "answer #2");
        Poll poll2 = TestUtil.newAnonymousPoll("title2");
        Facade.Polls.addAnswers(poll2, "answer #3");
        assertEquals(2, Facade.Polls.getAnswers(poll1).size());
        assertEquals(1, Facade.Polls.getAnswers(poll2).size());
    }
}
