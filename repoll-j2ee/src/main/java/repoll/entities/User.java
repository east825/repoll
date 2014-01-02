package repoll.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author Mikhail Golubev
 */

@Entity
// plain "User" is reserved word in SQL
@Table(name = "\"User\"")
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
    private String password;
    //    @NotNull
    private String firstName = "";
    //    @NotNull
    private String lastName = "";
    //    @NotNull
    private String middleName = "";
    //    @NotNull
    private String additionalInfo = "";
    //    @NotNull
    private Long stackoverflowId = 0L;
    //    @NotNull
    private Date registrationDate;
    //    @NotNull
    private Date lastVisitDate;

    @Override
    @Id
    // also can use @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue
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


    @Column(nullable = true)
    public Long getStackoverflowId() {
        return stackoverflowId;
    }

    public void setStackoverflowId(Long id) {
        this.stackoverflowId = id;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registrationDateTime", nullable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date date) {
        this.registrationDate = date == null ? new Date() : date;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastVisitDateTime", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(Date date) {
        this.lastVisitDate = date == null ? new Date() : date;
    }
}
