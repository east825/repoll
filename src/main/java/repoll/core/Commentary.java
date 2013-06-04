package repoll.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/*
 * Only message field modification is legal
 */

public class Commentary extends DomainObject {
    private final User author;
    private final Poll poll;
    private String message;
    private final Date creationDate;

    public Commentary(User author, Poll poll, String message) {
        this(author, poll, message, new Date());
    }

    public Commentary(@Nullable User author, @NotNull Poll poll, @NotNull String message, @NotNull Date creationDate) {
        this.author = author;
        this.poll = poll;
        this.message = message;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return String.format("Commentary(id=%d message=%s)", getId(), message);
    }

    @Nullable
    public User getAuthor() {
        return author;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    @NotNull
    public Poll getPoll() {
        return poll;
    }

    @NotNull
    public Date getCreationDate() {
        return creationDate;
    }

}
