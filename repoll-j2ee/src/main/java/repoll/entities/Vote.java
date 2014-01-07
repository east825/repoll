package repoll.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Mikhail Golubev
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Vote.FIND_FOR_ANSWER, query = "select v from Vote v where v.answer = :answer")
})
public class Vote extends DomainObject {
    public static final String FIND_FOR_ANSWER = "Vote.findForAnswer";
    private long id;
    private Date creationDate;

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

    @PrePersist
    private void setTimestamps() {
        creationDate = new Date();
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creationDateTime", insertable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date date) {
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
