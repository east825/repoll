package repoll.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "title"))
@NamedQueries({
        @NamedQuery(name = Poll.FIND_BY_TITLE_FUZZY, query = "select p from Poll p where p.title like :title")
})
public class Poll extends DomainObject {
    public static final String FIND_BY_TITLE_FUZZY = "Poll.findByTitleFuzzy";

    private long id;
    private String title;
    private String description;
    private Date creationDate;

    private User author;
    private List<Answer> answers;
    private List<Commentary> commentaries;

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

    @OneToMany(mappedBy = "poll", orphanRemoval = true)
    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @OneToMany(mappedBy = "poll", orphanRemoval = true)
    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }
}
