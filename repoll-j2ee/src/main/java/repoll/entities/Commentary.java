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

    @Override
    @Id
    @GeneratedValue
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
    @Column(name = "creationDateTime", nullable = false, insertable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }
}
