package repoll.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Poll extends DomainObject {
    private User author;
    private String title, description = "";
    private Date creationDate = new Date(System.currentTimeMillis());
    private List<Answer> answers = new ArrayList<>();
    private List<Commentary> commentaries = new ArrayList<>();


    public Poll(User author, String title) {
        this.author = author;
        this.title = title;
    }

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
