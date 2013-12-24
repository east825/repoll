package repoll.server;

import org.junit.Ignore;
import org.junit.Test;
import repoll.models.Commentary;
import repoll.models.Poll;
import repoll.models.User;
import repoll.server.mappers.Facade;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;
import repoll.util.DatabaseUtil;

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
        Poll poll = Mappers.insert(new Poll(null, "title"));
        Commentary c1 = new Commentary(null, poll, "message1");
        Commentary c2 = new Commentary(null, poll, "message2");
        Mappers.insert(c1);
        assertTrue(c1.isSaved());
        assertSame(c1, Mappers.loadById(Commentary.class, c1.getId()));
        Mappers.insert(c2);
        assertTrue(c2.isSaved());
        assertSame(c2, Mappers.loadById(Commentary.class, c2.getId()));
        executeCountQueryAndCheckResult(COUNT_COMMENTARIES_QUERY, 2);
        Mappers.delete(c1);
        executeCountQueryAndCheckResult(COUNT_COMMENTARIES_QUERY, 1);
        Mappers.delete(c2);
        executeCountQueryAndCheckResult(COUNT_COMMENTARIES_QUERY, 0);
    }

    @Test
    public void updateCommentary() throws MapperException, SQLException {
        Date creationDate = new Date(100);
        Poll poll = Mappers.insert(new Poll(null, "title"));
        Commentary commentary = Mappers.insert(new Commentary(null, poll, "message1", creationDate));
        try (PreparedStatement statement = testConnection.prepareStatement(SELECT_ALL_FIELDS_QUERY)) {
            statement.setLong(1, commentary.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals(resultSet.getObject("user_id"), null);
            assertEquals(resultSet.getString("message"), "message1");
            assertEquals(resultSet.getLong("poll_id"), poll.getId());
            assertEquals(resultSet.getTimestamp("creation_datetime"), DatabaseUtil.dateToTimestamp(creationDate));

            commentary.setMessage("message2");
            Mappers.update(commentary);

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
        Poll poll = Mappers.insert(new Poll(null, "title"));
        // Not inserted
        User author = User.newFromCredentials("login", "passwd");
        Facade.Users.comment(author, poll, "message");
    }

    @Test(expected = IllegalStateException.class)
    public void pollNotInsertedBeforeCommentary() throws MapperException {
        // Not inserted
        Poll poll = new Poll(null, "title");
        Facade.Users.comment(null, poll, "message");
    }

    @SuppressWarnings("ConstantConditions")
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
