package repoll.core;

import org.junit.After;
import org.junit.Test;
import repoll.mappers.MapperException;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PollTest extends DatabaseTest {
    @After
    public void setUp() {
        clearTestDatabase();
    }

    @Test
    public void createPoll() throws MapperException, SQLException {
        User author = User.builder("login", "passwd").build();
        author.insert();
        Poll poll = new Poll(author, "title");
        poll.insert();
        Statement statement = ConnectionProvider.connection().createStatement();
        assertTrue(statement.executeQuery("select * from \"User\"").next());
        assertTrue(statement.executeQuery("select * from \"Poll\"").next());
        author.delete();
        poll.delete();
        assertFalse(statement.executeQuery("select * from \"User\"").next());
        assertFalse(statement.executeQuery("select * from \"Poll\"").next());
    }

    @Test(expected = IllegalStateException.class)
    public void pollAuthorNotInsertedBefore() throws MapperException {
        User author = User.builder("login", "passwd").build();
        Poll poll = new Poll(author, "title");
        poll.insert();
    }
}
