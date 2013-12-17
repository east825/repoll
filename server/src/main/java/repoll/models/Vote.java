package repoll.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.server.mappers.AbstractMapper;
import repoll.server.mappers.Mappers;

import java.util.Date;

/*
 * This object should be considered immutable
 */
public class Vote extends DomainObject {
    private final User author;
    private final Answer answer;
    private final Date creationDate;

    public Vote(User author, Answer answer) {
        this(author, answer, new Date());
    }

    public Vote(@Nullable User author, @NotNull Answer answer, @NotNull Date creationDate) {
        this.author = author;
        this.answer = answer;
        this.creationDate = creationDate;
    }

    public static AbstractMapper<Vote> getMapper() {
        return Mappers.getForClass(Vote.class);
    }

    @Override
    public String toString() {
        return String.format("Vote(id=%d user=%s answer=%s)", getId(),
                author == null ? null : author.getLogin(),
                answer.getDescription()
        );
    }

    @Nullable
    public User getAuthor() {
        return author;
    }

    @NotNull
    public Answer getAnswer() {
        return answer;
    }

    @NotNull
    public Date getCreationDate() {
        return creationDate;
    }

}
