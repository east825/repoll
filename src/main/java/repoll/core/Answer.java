package repoll.core;

import org.jetbrains.annotations.NotNull;
import repoll.mappers.AbstractMapper;
import repoll.mappers.MapperException;
import repoll.mappers.Mappers;

import java.util.List;

public class Answer extends DomainObject {

    private final Poll poll;
    private String description;

    public Answer(@NotNull Poll poll, @NotNull String description) {
        this.poll = poll;
        this.description = description;
    }

    public static AbstractMapper<Answer> getMapper() {
        return Mappers.getForClass(Answer.class);
    }

    @NotNull
    public Poll getPoll() {
        return poll;
    }

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
