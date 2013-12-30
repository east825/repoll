package repoll.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * @author Mikhail Golubev
 */
@Entity
public class Vote extends DomainObject {
    private long id;
    private Timestamp creationDate;

    @Override
    @Id
    @Column(nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "creationDateTime", nullable = false)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp date) {
        this.creationDate = date;
    }
}
