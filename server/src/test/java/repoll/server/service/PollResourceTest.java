package repoll.server.service;

import com.google.gson.reflect.TypeToken;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;
import repoll.TestUtil;
import repoll.models.Poll;
import repoll.server.mappers.MapperException;
import repoll.server.rest.PollsResource;

import javax.ws.rs.core.Application;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static repoll.server.rest.PollsResource.GSON;

public class PollResourceTest extends JerseyTest {
    private static final Type LIST_OF_POLLS_TYPE = new TypeToken<List<Poll>>() {
    }.getType();
    private static Connection testConnection;

    @BeforeClass
    public static void connectTestDatabase() {
        testConnection = TestUtil.initializeTestDatabaseConnection();
    }

    @AfterClass
    public static void closeConnection() {
        TestUtil.closeConnection(testConnection);
    }

    @Before
    public void clearTestDatabaseBefore() {
        TestUtil.clearDatabase(testConnection);
    }

    @After
    public void clearTestDatabaseAfter() {
        TestUtil.clearDatabase(testConnection);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(PollsResource.class);
    }

    @Test
    public void getAllPolls() throws MapperException {
        Poll poll1 = TestUtil.newAnonymousPoll("title1");
        Poll poll2 = TestUtil.newAnonymousPoll("title2");
        Poll poll3 = TestUtil.newAnonymousPoll("title3");
        String response = getAllPollsAsJsonString();
        List<Poll> receivedPolls = GSON.fromJson(response, LIST_OF_POLLS_TYPE);
        assertEquals(Arrays.asList(poll1, poll2, poll3), receivedPolls);
    }

    @Test
    public void getMostCommentedPoll() throws MapperException {
        Poll mostCommented = TestUtil.newAnonymousPoll("Most commented");
        Poll poll1 = TestUtil.newAnonymousPoll("other poll #1");
        Poll poll2 = TestUtil.newAnonymousPoll("other poll #2");
        TestUtil.newAnonymousCommentary(mostCommented, "comment #1");
        TestUtil.newAnonymousCommentary(mostCommented, "comment #2");
        TestUtil.newAnonymousCommentary(mostCommented, "comment #3");
        TestUtil.newAnonymousCommentary(poll1, "comment #4");
        TestUtil.newAnonymousCommentary(poll2, "comment #5");
        String response = getMostCommentedPollAsJsonString();
        assertEquals(mostCommented, GSON.fromJson(response, Poll.class));
    }

    @Test
    public void noPolls() {
        String allPollsAsJsonString = getAllPollsAsJsonString();
        List<Poll> allPolls = GSON.fromJson(allPollsAsJsonString, LIST_OF_POLLS_TYPE);
        assertTrue(allPolls.isEmpty());
        assertEquals("no polls", getMostCommentedPollAsJsonString());
    }

    private String getMostCommentedPollAsJsonString() {
        return target("/polls/most-commented/").request().get(String.class);
    }

    private String getAllPollsAsJsonString() {
        return target("/polls/").request().get(String.class);
    }

}
