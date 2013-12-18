package repoll.server;

import org.junit.Ignore;
import org.junit.Test;
import repoll.models.Answer;
import repoll.models.Poll;
import repoll.models.Vote;
import repoll.server.mappers.AbstractMapper;
import repoll.server.mappers.Facade;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class AnswerTest extends DatabaseTest {

    private static final String COUNT_ANSWERS_QUERY = "select count(id) from \"Answer\"";
    private static final String SELECT_DESCRIPTION_QUERY = "select description from \"Answer\" where id = ?";

    @Test
    public void insertAndDeleteAnswer() throws MapperException, SQLException {
        AbstractMapper<Answer> mapper = Mappers.getForClass(Answer.class);
        Poll poll = new Poll(null, "title");
        Mappers.insert(poll);
        Answer answer1 = new Answer(poll, "Answer #1");
        Answer answer2 = new Answer(poll, "Answer #2");
        Mappers.insert(answer1);
        Mappers.insert(answer2);
        assertSame(answer1, mapper.loadById(answer1.getId()));
        assertSame(answer2, mapper.loadById(answer2.getId()));
        executeCountQueryAndCheckResult(COUNT_ANSWERS_QUERY, 2);
        Mappers.delete(answer1);
        executeCountQueryAndCheckResult(COUNT_ANSWERS_QUERY, 1);
        Mappers.delete(answer2);
        executeCountQueryAndCheckResult(COUNT_ANSWERS_QUERY, 0);
    }

    @Test
    public void insertAndUpdateAnswer() throws MapperException, SQLException {
        Poll poll = new Poll(null, "title");
        Mappers.insert(poll);
        Answer answer = new Answer(poll, "Answer #1");
        Mappers.insert(answer);
        try (PreparedStatement statement = testConnection.prepareStatement(SELECT_DESCRIPTION_QUERY)) {
            statement.setLong(1, answer.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("Answer #1", resultSet.getString(1));
            answer.setDescription("Answer #2");
            Mappers.update(answer);
            resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("Answer #2", resultSet.getString(1));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void pollNotInsertedBeforeAnswer() throws MapperException {
        Poll poll = new Poll(null, "title");
        Answer answer = new Answer(poll, "Answer #1");
        Mappers.insert(answer);
    }

    @Test(expected = MapperException.class)
    public void conflictingDescription() throws MapperException {
        Poll poll = new Poll(null, "title");
        Mappers.insert(poll);
        Mappers.insert(new Answer(poll, "answer"));
        Mappers.insert(new Answer(poll, "answer"));
    }

    @Ignore
    @Test
    public void illegalParameters() {
        try {
            new Answer(null, "description");
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        Poll poll = new Poll(null, "title");
        try {
            new Answer(poll, null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            Answer answer = new Answer(poll, "description");
            answer.setDescription(null);
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void selectVotes() throws MapperException {
        Poll poll = Mappers.insert(new Poll(null, "title"));
        Answer answer1 = Mappers.insert(new Answer(poll, "answer #1"));
        Answer answer2 = Mappers.insert(new Answer(poll, "answer #2"));
        Facade.Users.vote(null, answer1);
        Facade.Users.vote(null, answer1);
        Facade.Users.vote(null, answer2);
        assertEquals(2, Facade.Answers.getVotes(answer1).size());
        assertEquals(1, Facade.Answers.getVotes(answer2).size());
    }
}
