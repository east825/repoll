package repoll.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Mikhail Golubev
 */
@Entity
public class Commentary extends DomainObject {
    private long id;
    private String message;
    private Date creationDate;

    private User author;
    private Poll poll;

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

    @Column(nullable = false, length = 3000)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "pollId", nullable = false)
    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
