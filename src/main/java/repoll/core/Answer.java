package repoll.core;

public class Answer extends DomainObject {
    private final Poll poll;
    private final String description;

    public Answer(Poll poll, String description) {
        this.poll = poll;
        this.description = description;
    }

    public Poll getPoll() {
        return poll;
    }

    public String getDescription() {
        return description;
    }
}
