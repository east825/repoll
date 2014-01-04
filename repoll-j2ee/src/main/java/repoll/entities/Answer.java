package repoll.entities;

import javax.persistence.*;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"pollId", "description"}))
public class Answer extends DomainObject {
    private long id;
    private String description;

    private Poll poll;
    private List<Vote> votes;

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

    @Column(nullable = false, length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "pollId", nullable = false)
    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @OneToMany(mappedBy = "answer", orphanRemoval = true)
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
