package repoll.core;

public class Vote {
    private final User author;
    private final Answer answer;

    public Vote(User author, Answer answer) {
        this.author = author;
        this.answer = answer;
    }
}
