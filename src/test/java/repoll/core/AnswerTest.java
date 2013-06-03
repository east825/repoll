package repoll.core;

import org.junit.Test;
import repoll.mappers.AbstractMapper;
import repoll.mappers.MapperException;
import repoll.mappers.Mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.Assert.*;

public class AnswerTest extends DatabaseTest {

    private static final String COUNT_ANSWERS_QUERY = "select count(id) from \"Answer\"";
    private static final String SELECT_DESCRIPTION_QUERY = "select description from \"Answer\" where id = ?";

    @Test
    public void insertAndDeleteAnswer() throws MapperException, SQLException {
        AbstractMapper<Answer> mapper = Mappers.getForClass(Answer.class);
        Poll poll = new Poll(null, "title");
        poll.insert();
        Answer answer1 = new Answer(poll, "Answer #1");
        Answer answer2 = new Answer(poll, "Answer #2");
        answer1.insert();
        answer2.insert();
        assertSame(answer1, mapper.loadById(answer1.getId()));
        assertSame(answer2, mapper.loadById(answer2.getId()));
        executeCountQueryAndCheckResult(COUNT_ANSWERS_QUERY, 2);
        answer1.delete();
        executeCountQueryAndCheckResult(COUNT_ANSWERS_QUERY, 1);
        answer2.delete();
        executeCountQueryAndCheckResult(COUNT_ANSWERS_QUERY, 0);
    }

    @Test
    public void insertAndUpdateAnswer() throws MapperException, SQLException {
        Poll poll = new Poll(null, "title");
        poll.insert();
        Answer answer = new Answer(poll, "Answer #1");
        answer.insert();
        try (PreparedStatement statement = testConnection.prepareStatement(SELECT_DESCRIPTION_QUERY)) {
            statement.setLong(1, answer.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("Answer #1", resultSet.getString(1));
            answer.setDescription("Answer #2");
            answer.update();
            resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("Answer #2", resultSet.getString(1));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void pollNotInsertedBeforeAnswer() throws MapperException {
        Poll poll = new Poll(null, "title");
        Answer answer = new Answer(poll, "Answer #1");
        answer.insert();
    }

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


}
