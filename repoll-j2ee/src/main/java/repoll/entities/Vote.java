package repoll.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Mikhail Golubev
 */
@Entity
public class Vote extends DomainObject {
    private long id;
    private Timestamp creationDate;

    private User author;
    private Answer answer;

    /**
     * Serialization constructor
     */
    public Vote() {
        // empty
    }

    public Vote(User author, Answer answer) {
        this.answer = answer;
        this.author = author;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "creationDateTime", insertable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp date) {
        this.creationDate = date;
    }

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    @ManyToOne
    @JoinColumn(name = "answerId", nullable = false)
    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
