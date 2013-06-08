package repoll.core;

import org.junit.Test;
import repoll.DatabaseTest;
import repoll.mappers.MapperException;

import java.sql.*;
import java.util.Date;

import static org.junit.Assert.*;

public class PollTest extends DatabaseTest {
    private static final String SELECT_ALL_FIELDS_QUERY = "select * from \"Poll\" where id = ?";

    @Test
    public void insertAndDeletePoll() throws MapperException, SQLException {
        User author = User.builder("login", "passwd").build();
        author.insert();
        Poll poll = new Poll(author, "title");
        poll.insert();
        assertTrue(poll.isSaved());
        Statement statement = testConnection.createStatement();
        assertTrue(statement.executeQuery("select * from \"User\"").next());
        assertTrue(statement.executeQuery("select * from \"Poll\"").next());
        author.delete();
        poll.delete();
        assertFalse(poll.isSaved());
        assertFalse(statement.executeQuery("select * from \"User\"").next());
        assertFalse(statement.executeQuery("select * from \"Poll\"").next());
    }

    @Test
    public void updatePoll() throws MapperException, SQLException {
        Date creationDate = new Date(100);
        Poll poll = new Poll(null, "title1", "description1", creationDate);
        poll.insert();
        try (PreparedStatement statement = testConnection.prepareStatement(SELECT_ALL_FIELDS_QUERY)) {
            statement.setLong(1, poll.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("title1", resultSet.getString("title"));
            assertEquals("description1", resultSet.getString("description"));
            assertEquals(creationDate, resultSet.getTimestamp("creation_datetime"));

            poll.setTitle("title2");
            poll.setDescription("description2");
            poll.update();

            resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("title2", resultSet.getString("title"));
            assertEquals("description2", resultSet.getString("description"));
            assertEquals(creationDate, resultSet.getTimestamp("creation_datetime"));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void authorNotInsertedBeforePoll() throws MapperException {
        User author = User.builder("login", "passwd").build();
        new Poll(author, "title").insert();
    }

    @Test(expected = MapperException.class)
    public void conflictingTitle() throws MapperException {
        new Poll(null, "title").insert();
        new Poll(null, "title").insert();
    }

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
        Poll poll1 = Util.newAnonymousPoll("titlel");
        Poll poll2 = Util.newAnonymousPoll("title2");
        Util.newAnonymousCommentary(poll1, "commentary #1");
        Util.newAnonymousCommentary(poll1, "commentary #2");
        Util.newAnonymousCommentary(poll2, "commentary #3");
        assertEquals(2, poll1.getCommentaries().size());
        assertEquals(1, poll2.getCommentaries().size());
    }

    @Test
    public void selectAnswers() throws MapperException {
        Poll poll1 = Util.newAnonymousPoll("title1");
        poll1.addAnswers("answer #1", "answer #2");
        Poll poll2 = Util.newAnonymousPoll("title2");
        poll2.addAnswer("answer #3");
        assertEquals(2, poll1.getAnswers().size());
        assertEquals(1, poll2.getAnswers().size());
    }
}
