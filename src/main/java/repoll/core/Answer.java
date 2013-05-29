package repoll.core;

public class Answer {
    private final Poll poll;
    private final String description;

    public Answer(Poll poll, String description) {
        this.poll = poll;
        this.description = description;
    }
}
