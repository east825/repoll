package repoll.models;

import org.jetbrains.annotations.NotNull;
import repoll.models.views.AnswerView;
import repoll.server.mappers.AbstractMapper;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.util.List;

public class Answer extends DomainObject implements AnswerView {

    private final Poll poll;
    private String description;

    public Answer(@NotNull Poll poll, @NotNull String description) {
        this.poll = poll;
        this.description = description;
    }

    public static AbstractMapper<Answer> getMapper() {
        return Mappers.getForClass(Answer.class);
    }

    @Override
    public String toString() {
        return String.format("Answer(id=%d title='%s')", getId(), description);
    }

    @Override
    @NotNull
    public Poll getPoll() {
        return poll;
    }

    @Override
    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    public List<Vote> getVotes() throws MapperException {
        return Mappers.getForClass(Vote.class).selectRelated(this);
    }

    public int getVotesNumber() throws MapperException {
        return getVotes().size();
    }
}
