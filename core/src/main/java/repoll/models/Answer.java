package repoll.models;

import org.jetbrains.annotations.NotNull;

public final class Answer extends DomainObject {
    static final long serialVersionUID = 6534468183505541126L;

    private final Poll poll;
    private String description;

    public Answer(@NotNull Poll poll, @NotNull String description) {
        this.poll = poll;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Answer(id=%d title='%s')", getId(), description);
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
