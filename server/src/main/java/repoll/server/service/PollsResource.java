package repoll.server.service;

import repoll.models.Poll;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/polls")
public class PollsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String allPolls() throws Exception {
        return ServiceUtil.GSON.toJson(Poll.getMapper().all());
    }

    @GET
    @Path("/most-commented")
    @Produces(MediaType.APPLICATION_JSON)
    public String mostCommentedPoll() throws Exception {
        List<Poll> allPolls = Poll.getMapper().all();
        if (allPolls.isEmpty()) {
            return "no polls";
        }
        Poll mostCommented = allPolls.get(0);
        for (Poll poll : allPolls) {
            if (poll.getCommentaries().size() > mostCommented.getCommentaries().size()) {
                mostCommented = poll;
            }
        }
        return ServiceUtil.GSON.toJson(mostCommented);
    }
}
