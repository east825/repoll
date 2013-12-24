package repoll.server;

import org.junit.Ignore;
import org.junit.Test;
import repoll.models.Answer;
import repoll.models.Poll;
import repoll.models.User;
import repoll.models.Vote;
import repoll.server.mappers.AbstractMapper;
import repoll.server.mappers.Facade;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;
import static repoll.server.mappers.Facade.Users;


public class VoteTest extends DatabaseTest {

    private static final String COUNT_VOTES_QUERY = "select count(id) from \"Vote\"";

    @Test
    public void insertAndDeleteVote() throws MapperException, SQLException {
        User user1 = Users.createFromCredentials("login1", "passwd");
        User user2 = Users.createFromCredentials("login2", "passwd");
        Poll poll = Mappers.insert(new Poll(null, "title"));
        Answer answer1 = Facade.Polls.addAnswer(poll, "answer #1");
        Answer answer2 = Facade.Polls.addAnswer(poll, "answer #2");
        Vote vote1 = Users.vote(user1, answer1);
        AbstractMapper<Vote> mapper = Mappers.getForClass(Vote.class);
        assertSame(vote1, mapper.loadById(vote1.getId()));
        assertTrue(vote1.isSaved());
        Vote vote2 = Users.vote(user2, answer2);
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
        // Not inserted
        User author = User.newFromCredentials("login", "passwd");
        Poll poll = Facade.Users.createPoll(null, "title");
        Answer answer = Facade.Polls.addAnswer(poll, "answer1");
        Facade.Users.vote(author, answer);
    }

    @Test(expected = IllegalStateException.class)
    public void answerNotInsertedBeforeVote() throws MapperException {
        User author = Users.createFromCredentials("login", "passwd");
        Poll poll = Facade.Users.createPoll(null, "title");
        // Not inserted
        Answer answer = new Answer(poll, "answer1");
        Facade.Users.vote(author, answer);
    }

    @SuppressWarnings("ConstantConditions")
    @Ignore
    @Test
    public void illegalParameters() throws MapperException {
        User author = User.newFromCredentials("login", "passwd");
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
