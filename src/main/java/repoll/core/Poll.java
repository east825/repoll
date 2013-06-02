package repoll.core;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Poll extends DomainObject {
    private final User author;
    private String title, description;
    private final Date creationDate;

    public Poll(User author, String title) {
        this(author, title, "");
    }

    public Poll(User author, String title, String description) {
        this(author, title, description, new Date(System.currentTimeMillis()));
    }

    public Poll(User author, String title, String description, Date creationDate) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return String.format("Poll(id=%d title=%s)", getId(), title);
    }

    public List<Commentary> getCommentaries() {
        return Collections.emptyList();
    }

    public List<Answer> getAnswers() {
        return Collections.emptyList();
    }

    public User getAuthor() {
        return author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
