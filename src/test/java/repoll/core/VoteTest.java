package repoll.core;

import org.junit.Test;
import repoll.mappers.AbstractMapper;
import repoll.mappers.MapperException;

import java.sql.SQLException;
import java.util.Date;

import static junit.framework.Assert.*;

public class VoteTest extends DatabaseTest {

    private static final String COUNT_VOTES_QUERY = "select count(id) from \"Vote\"";

    @Test
    public void insertAndDeleteVote() throws MapperException, SQLException {
        User user1 = User.builder("login1", "passwd").build();
        user1.insert();
        User user2 = User.builder("login2", "passwd").build();
        user2.insert();
        Poll poll = new Poll(null, "title");
        poll.insert();
        Answer answer1 = new Answer(poll, "answer #1");
        answer1.insert();
        Answer answer2 = new Answer(poll, "answer #2");
        answer2.insert();
        Vote vote1 = new Vote(user1, answer1);
        vote1.insert();
        AbstractMapper<Vote> mapper = Vote.getMapper();
        assertSame(vote1, mapper.loadById(vote1.getId()));
        assertTrue(vote1.isSaved());
        Vote vote2 = new Vote(user2, answer2);
        vote2.insert();
        assertTrue(vote2.isSaved());
        assertSame(vote2, mapper.loadById(vote2.getId()));
        executeCountQueryAndCheckResult(COUNT_VOTES_QUERY, 2);
        vote1.delete();
        assertFalse(vote1.isSaved());
        executeCountQueryAndCheckResult(COUNT_VOTES_QUERY, 1);
        vote2.delete();
        assertFalse(vote2.isSaved());
        executeCountQueryAndCheckResult(COUNT_VOTES_QUERY, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void authorNotInsertedBeforeVote() throws MapperException {
        User author = User.builder("login", "passwd").build();
        Poll poll = new Poll(null, "title");
        poll.insert();
        Answer answer = new Answer(poll, "answer1");
        answer.insert();
        new Vote(author, answer).insert();
    }

    @Test(expected = IllegalStateException.class)
    public void answerNotInsertedBeforeVote() throws MapperException {
        User author = User.builder("login", "passwd").build();
        author.insert();
        Poll poll = new Poll(null, "title");
        poll.insert();
        Answer answer = new Answer(poll, "answer1");
        new Vote(author, answer).insert();
    }

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
