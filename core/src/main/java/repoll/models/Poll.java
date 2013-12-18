package repoll.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public final class Poll extends DomainObject {
    private final User author;
    private String title, description;
    private final Date creationDate;

    public Poll(User author, String title) {
        this(author, title, "");
    }

    public Poll(User author, String title, String description) {
        this(author, title, description, new Date());
    }

    public Poll(@Nullable User author, @NotNull String title, @NotNull String description, @NotNull Date creationDate) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return String.format("Poll(id=%d title='%s')", getId(), title);
    }

    @Nullable
    public User getAuthor() {
        return author;
    }

    @NotNull
    public Date getCreationDate() {
        return creationDate;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }
}
