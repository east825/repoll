package repoll.core;

public class Vote implements DatabasePersistent<Vote> {
    private final User author;
    private final Answer answer;

    public Vote(User author, Answer answer) {
        this.author = author;
        this.answer = answer;
    }

    @Override
    public void save() {
    }

    @Override
    public Vote load() {
        return null;
    }
}
