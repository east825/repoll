package repoll.server;

import org.junit.Ignore;
import org.junit.Test;
import repoll.models.Commentary;
import repoll.models.Poll;
import repoll.models.User;
import repoll.server.mappers.AbstractMapper;
import repoll.util.DatabaseUtil;
import repoll.server.mappers.MapperException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

public class CommentaryTest extends DatabaseTest {

    private static final String COUNT_COMMENTARIES_QUERY = "select count(id) from \"Commentary\"";
    private static final String SELECT_ALL_FIELDS_QUERY = "select * from \"Commentary\" where id = ?";

    @Test
    public void insertAndDeleteCommentary() throws MapperException, SQLException {
        Poll poll = new Poll(null, "title");
        poll.insert();
        Commentary c1 = new Commentary(null, poll, "message1");
        Commentary c2 = new Commentary(null, poll, "message2");
        AbstractMapper<Commentary> mapper = Commentary.getMapper();
        c1.insert();
        assertTrue(c1.isSaved());
        assertSame(c1, mapper.loadById(c1.getId()));
        c2.insert();
        assertTrue(c2.isSaved());
        assertSame(c2, mapper.loadById(c2.getId()));
        executeCountQueryAndCheckResult(COUNT_COMMENTARIES_QUERY, 2);
        c1.delete();
        executeCountQueryAndCheckResult(COUNT_COMMENTARIES_QUERY, 1);
        c2.delete();
        executeCountQueryAndCheckResult(COUNT_COMMENTARIES_QUERY, 0);
    }

    @Test
    public void updateCommentary() throws MapperException, SQLException {
        Date creationDate = new Date(100);
        Poll poll = new Poll(null, "title");
        poll.insert();
        Commentary commentary = new Commentary(null, poll, "message1", creationDate);
        commentary.insert();
        try (PreparedStatement statement = testConnection.prepareStatement(SELECT_ALL_FIELDS_QUERY)) {
            statement.setLong(1, commentary.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals(resultSet.getObject("user_id"), null);
            assertEquals(resultSet.getString("message"), "message1");
            assertEquals(resultSet.getLong("poll_id"), poll.getId());
            assertEquals(resultSet.getTimestamp("creation_datetime"), DatabaseUtil.dateToTimestamp(creationDate));

            commentary.setMessage("message2");
            commentary.update();

            resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals(resultSet.getObject("user_id"), null);
            assertEquals(resultSet.getString("message"), "message2");
            assertEquals(resultSet.getLong("poll_id"), poll.getId());
            assertEquals(resultSet.getTimestamp("creation_datetime"), DatabaseUtil.dateToTimestamp(creationDate));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void authorNotInsertedBeforeCommentary() throws MapperException {
        Poll poll = new Poll(null, "title");
        poll.insert();
        User author = User.builder("login", "passwd").build();
        new Commentary(author, poll, "message").insert();
    }

    @Test(expected = IllegalStateException.class)
    public void pollNotInsertedBeforeCommentary() throws MapperException {
        Poll poll = new Poll(null, "title");
        new Commentary(null, poll, "message").insert();
    }

    @Ignore
    @Test
    public void illegalParameters() {
        User user = User.builder("login", "passwd").build();
        Poll poll = new Poll(user, "title");
        Date date = new Date();
        try {
            new Commentary(user, null, "message", date);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            new Commentary(user, poll, null, date);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            new Commentary(user, poll, "message", null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            Commentary commentary = new Commentary(user, poll, "message", date);
            commentary.setMessage(null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
