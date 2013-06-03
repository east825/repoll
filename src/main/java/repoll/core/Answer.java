package repoll.core;

import org.jetbrains.annotations.NotNull;

public class Answer extends DomainObject {
    private final Poll poll;
    private String description;

    public Answer(@NotNull Poll poll, @NotNull String description) {
        this.poll = poll;
        this.description = description;
    }

    @NotNull
    public Poll getPoll() {
        return poll;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }
}
