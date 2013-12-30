package repoll.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Mikhail Golubev
 */
@Entity
public class Poll extends DomainObject {
    private long id;
    private String title;
    private String description;
    private Date creationDate;

    @Override
    @Id
    @Column(nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false, length = 3000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creationDateTime", nullable = false, insertable = true, length = 29)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

}
