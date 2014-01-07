package repoll.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * @author Mikhail Golubev
 */
@Entity
@Table(
        name = "\"User\"", // plain "User" is reserved word in SQL
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "login"),
                @UniqueConstraint(columnNames = "email")
        }
)
@NamedQueries({
        @NamedQuery(name = User.FIND_ALL, query = "select u from User u"),
        @NamedQuery(name = User.FIND_BY_CREDENTIALS, query = "select u from User u where u.login = :login and u.password = :password"),
        @NamedQuery(name = User.FIND_BY_LOGIN, query = "select u from User u where u.login = :login")
})
public class User extends DomainObject {
    public static final String FIND_ALL = "User.findAll";
    public static final String FIND_BY_CREDENTIALS = "User.findByCredentials";
    public static final String FIND_BY_LOGIN = "User.findByLogin";

    private long id;
    //    @NotNull
    @Pattern(regexp = "[\\w-]+", message = "Login contains illegal symbols")
    private String login;
    @Pattern(regexp = ".+", message = "Password can't be empty")
//    @NotNull
    private String password = "";
//    @NotNull
    private String firstName = "";
//    @NotNull
    private String lastName = "";
//    @NotNull
    private String middleName = "";
//    @NotNull
    private String additionalInfo = "";
//    @NotNull
    private Date registrationDate;
//    @NotNull
    private Date lastVisitDate;
//    @NotNull
    private String email;
    private Date dateOfBirth;

    private List<Poll> polls;
    private List<Commentary> commentaries;
    private List<Vote> votes;

    @PrePersist
    private void setTimestamps() {
        registrationDate = lastVisitDate = new Date();
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

    @Column(nullable = false, length = 20, unique = true)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    @Column(nullable = false, length = 30)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Column(nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    @Column(nullable = false, length = 30)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Column(nullable = false, length = 30)
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    @Column(nullable = false, length = 3000)
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registrationDateTime", insertable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date date) {
        this.registrationDate = date == null ? new Date() : date;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastVisitDateTime", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(Date date) {
        this.lastVisitDate = date == null ? new Date() : date;
    }

    @Column(nullable = false, length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(value = TemporalType.DATE)
    @Column(nullable = true)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @OrderBy("creationDate DESC")
    @OneToMany(mappedBy = "author")
    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    @OrderBy("creationDate DESC")
    @OneToMany(mappedBy = "author")
    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }

    @OneToMany(mappedBy = "author")
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public void comment(Poll poll, String message) {
        Commentary commentary = new Commentary();
        commentary.setMessage(message);
        getCommentaries().add(commentary);
        poll.getCommentaries().add(commentary);
    }

    public void vote(Answer answer) {
        Vote vote = new Vote();
        getVotes().add(vote);
        answer.getVotes().add(vote);
    }
}
