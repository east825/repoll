package repoll.core;

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
        this(author, poll, message, new Date(System.currentTimeMillis()));
    }

    public Commentary(User author, Poll poll, String message, Date creationDate) {
        this.author = author;
        this.poll = poll;
        this.message = message;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return String.format("Commentary(id=%d message=%s)", getId(), message);
    }

    public User getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Poll getPoll() {
        return poll;
    }

    public Date getCreationDate() {
        return creationDate;
    }

}
