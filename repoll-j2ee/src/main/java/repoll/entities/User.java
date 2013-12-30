package repoll.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Mikhail Golubev
 */

@Entity
// plain "User" is reserved word in SQL
@Table(name = "\"User\"")
@NamedQueries({
        @NamedQuery(name = User.FIND_ALL, query = "select u from User u"),
        @NamedQuery(name = User.FIND_BY_CREDENTIALS, query = "select u from User u where u.login = :login and u.password = :password")
})
public class User extends DomainObject {
    public static final String FIND_ALL = "User.findAll";
    public static final String FIND_BY_CREDENTIALS = "User.findByCredentials";

    private long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String additionalInfo;
    private Long stackoverflowId;
    private Date registrationDate;
    private Date lastVisitDate;

    @Override
    @Id
    @Column(nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false, length = 20)
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
    @Column(name = "registrationDateTime", nullable = false)
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date date) {
        this.registrationDate = date;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastVisitDateTime", nullable = false)
    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(Date date) {
        this.lastVisitDate = date;
    }
}
