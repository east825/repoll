package repoll.server.rest;

import repoll.models.Poll;
import repoll.server.mappers.Mappers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static repoll.server.mappers.Facade.Polls;

@Path("/polls")
public class PollsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String allPolls() throws Exception {
        return ServiceUtil.GSON.toJson(Mappers.getForClass(Poll.class).all());
    }

    @GET
    @Path("/most-commented")
    @Produces(MediaType.APPLICATION_JSON)
    public String mostCommentedPoll() throws Exception {
        List<Poll> allPolls = Mappers.getForClass(Poll.class).all();
        if (allPolls.isEmpty()) {
            return "no polls";
        }
        Poll mostCommented = allPolls.get(0);
        for (Poll poll : allPolls) {
            if (Polls.getCommentaries(poll).size() > Polls.getCommentaries(mostCommented).size()) {
                mostCommented = poll;
            }
        }
        return ServiceUtil.GSON.toJson(mostCommented);
    }
}
