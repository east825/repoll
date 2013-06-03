package repoll.core;

import org.junit.Before;
import org.junit.Test;
import repoll.mappers.MapperException;

import java.sql.*;
import java.util.Date;

import static org.junit.Assert.*;;

public class PollTest extends DatabaseTest {
    private static final String SELECT_ALL_FIELDS_QUERY = "select * from \"Poll\" where id = ?";

    @Before
    public void setUp() {
        clearTestDatabase();
    }

    @Test
    public void insertAndDeletePoll() throws MapperException, SQLException {
        User author = User.builder("login", "passwd").build();
        author.insert();
        Poll poll = new Poll(author, "title");
        poll.insert();
        assertTrue(poll.isSaved());
        Statement statement = ConnectionProvider.connection().createStatement();
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
        Connection connection = ConnectionProvider.connection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_FIELDS_QUERY)) {
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
    public void pollAuthorNotInsertedBefore() throws MapperException {
        User author = User.builder("login", "passwd").build();
        Poll poll = new Poll(author, "title");
        poll.insert();
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
}
