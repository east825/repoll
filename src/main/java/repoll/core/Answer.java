package repoll.core;

public class Answer implements DatabasePersistent<Answer> {
    private final Poll poll;
    private final String description;

    public Answer(Poll poll, String description) {
        this.poll = poll;
        this.description = description;
    }

    @Override
    public void save() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Answer load() {
        throw new AssertionError("Not implemented");
    }
}
