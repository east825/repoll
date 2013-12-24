package repoll.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * This object should be considered immutable
 */
public final class Vote extends DomainObject {
    static final long serialVersionUID = 8923894830609306979L;

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
