package repoll.service;

import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import repoll.DatabaseTest;
import repoll.core.Poll;
import repoll.core.Util;
import repoll.mappers.MapperException;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class PollResourceTest extends DatabaseTest {
    private static final Type LIST_OF_POLLS_TYPE = new TypeToken<List<Poll>>() {}.getType();
    private static HttpServer server;

    @BeforeClass
    public static void startTestServer() throws Exception {
        server = GrizzlyServerFactory.createHttpServer("http://localhost:8000",
                new ClassNamesResourceConfig(PollsResource.class));
    }

    @AfterClass
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void getAllPolls() throws MapperException {
        Poll poll1 = Util.newAnonymousPoll("title1");
        Poll poll2 = Util.newAnonymousPoll("title2");
        Poll poll3 = Util.newAnonymousPoll("title3");
        String response = Client.create()
                .resource("http://localhost:8000/polls/")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        List<Poll> receivedPolls = ServiceUtil.GSON.fromJson(response, LIST_OF_POLLS_TYPE);
        assertEquals(Arrays.asList(poll1, poll2, poll3), receivedPolls);
    }

    @Test
    public void getMostCommentedPoll() throws MapperException {
        Poll mostCommented = Util.newAnonymousPoll("Most commented");
        Poll poll1 = Util.newAnonymousPoll("other poll #1");
        Poll poll2 = Util.newAnonymousPoll("other poll #2");
        Util.newAnonymousCommentary(mostCommented, "comment #1");
        Util.newAnonymousCommentary(mostCommented, "comment #2");
        Util.newAnonymousCommentary(mostCommented, "comment #3");
        Util.newAnonymousCommentary(poll1, "comment #4");
        Util.newAnonymousCommentary(poll2, "comment #5");
        String response = Client.create()
                .resource("http://localhost:8000/polls/most-commented")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        assertEquals(mostCommented, ServiceUtil.GSON.fromJson(response, Poll.class));

    }

}
