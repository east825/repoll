package repoll.core;

import java.util.Date;
import java.util.List;

public class Poll {
    private User author;
    private String title, description;
    private Date creationDate;
    private List<Answer> answers;
    private List<Commentary> commentaries;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descriptikkon) {
        this.description = description;
    }

    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
