package repoll.server;

import org.junit.Ignore;
import org.junit.Test;
import repoll.models.Answer;
import repoll.models.Poll;
import repoll.models.User;
import repoll.models.Vote;
import repoll.server.mappers.AbstractMapper;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

public class VoteTest extends DatabaseTest {

    private static final String COUNT_VOTES_QUERY = "select count(id) from \"Vote\"";

    @Test
    public void insertAndDeleteVote() throws MapperException, SQLException {
        User user1 = User.builder("login1", "passwd").build();
        Mappers.insert(user1);
        User user2 = User.builder("login2", "passwd").build();
        Mappers.insert(user2);
        Poll poll = new Poll(null, "title");
        Mappers.insert(poll);
        Answer answer1 = new Answer(poll, "answer #1");
        Mappers.insert(answer1);
        Answer answer2 = new Answer(poll, "answer #2");
        Mappers.insert(answer2);
        Vote vote1 = new Vote(user1, answer1);
        Mappers.insert(vote1);
        AbstractMapper<Vote> mapper = Mappers.getForClass(Vote.class);
        assertSame(vote1, mapper.loadById(vote1.getId()));
        assertTrue(vote1.isSaved());
        Vote vote2 = new Vote(user2, answer2);
        Mappers.insert(vote2);
        assertTrue(vote2.isSaved());
        assertSame(vote2, mapper.loadById(vote2.getId()));
        executeCountQueryAndCheckResult(COUNT_VOTES_QUERY, 2);
        Mappers.delete(vote1);
        assertFalse(vote1.isSaved());
        executeCountQueryAndCheckResult(COUNT_VOTES_QUERY, 1);
        Mappers.delete(vote2);
        assertFalse(vote2.isSaved());
        executeCountQueryAndCheckResult(COUNT_VOTES_QUERY, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void authorNotInsertedBeforeVote() throws MapperException {
        User author = User.builder("login", "passwd").build();
        Poll poll = new Poll(null, "title");
        Mappers.insert(poll);
        Answer answer = new Answer(poll, "answer1");
        Mappers.insert(answer);
        Mappers.insert(new Vote(author, answer));
    }

    @Test(expected = IllegalStateException.class)
    public void answerNotInsertedBeforeVote() throws MapperException {
        User author = User.builder("login", "passwd").build();
        Mappers.insert(author);
        Poll poll = new Poll(null, "title");
        Mappers.insert(poll);
        Answer answer = new Answer(poll, "answer1");
        Mappers.insert(new Vote(author, answer));
    }

    @Ignore
    @Test
    public void illegalParameters() throws MapperException {
        User author = User.builder("login", "passwd").build();
        Poll poll = new Poll(author, "title");
        Answer answer = new Answer(poll, "Answer #1");
        Date date = new Date(); // pass
        try {
            new Vote(author, null, date);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            new Vote(author, answer, null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
