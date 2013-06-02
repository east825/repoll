package repoll.core;

public class Vote extends DomainObject {
    private final User author;
    private final Answer answer;

    public Vote(User author, Answer answer) {
        this.author = author;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return String.format("Vote(id=%d user=%s answer=%s)", getId(),
                author == null ? null : author.getLogin(),
                answer.getDescription()
        );
    }

    public User getAuthor() {
        return author;
    }

    public Answer getAnswer() {
        return answer;
    }
}
