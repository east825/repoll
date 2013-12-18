package repoll.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.models.views.PollView;
import repoll.server.mappers.AbstractMapper;
import repoll.server.mappers.MapperException;
import repoll.server.mappers.Mappers;

import java.util.Date;
import java.util.List;

public class Poll extends DomainObject implements PollView {
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

    public static AbstractMapper<Poll> getMapper() {
        return Mappers.getForClass(Poll.class);
    }

    @Override
    public String toString() {
        return String.format("Poll(id=%d title='%s')", getId(), title);
    }

    public List<Answer> addAnswers(String... answers) throws MapperException {
        for (String answer : answers) {
            addAnswer(answer);
        }
        return getAnswers();
    }

    public Answer addAnswer(String description) throws MapperException {
        Answer answer = new Answer(this, description);
        answer.insert();
        return answer;
    }

    @NotNull
    public List<Commentary> getCommentaries() throws MapperException {
        return Mappers.getForClass(Commentary.class).selectRelated(this);
    }

    @NotNull
    public List<Answer> getAnswers() throws MapperException {
        return Mappers.getForClass(Answer.class).selectRelated(this);
    }

    @Override
    @Nullable
    public User getAuthor() {
        return author;
    }

    @Override
    @NotNull
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    @Override
    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }
}
