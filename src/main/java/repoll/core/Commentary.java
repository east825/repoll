package repoll.core;

public class Commentary extends DomainObject {
    private final User author;
    private final Poll poll;
    private final String message;

    public Commentary(User author, Poll poll, String message) {
        this.author = author;
        this.poll = poll;
        this.message = message;
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

    public Poll getPoll() {
        return poll;
    }
}
