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
public class User extends DomainObject {
    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String additionalInfo;
    private Integer stackoverflowId;
    private Timestamp registrationDatetime;
    private Timestamp lastVisitDatetime;

    @Override
    @Id
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LOGIN")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "MIDDLE_NAME")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Basic
    @Column(name = "ADDITIONAL_INFO")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Basic
    @Column(name = "STACKOVERFLOW_ID")
    public Integer getStackoverflowId() {
        return stackoverflowId;
    }

    public void setStackoverflowId(Integer stackoverflowId) {
        this.stackoverflowId = stackoverflowId;
    }

    @Basic
    @Column(name = "REGISTRATION_DATETIME")
    public Timestamp getRegistrationDatetime() {
        return registrationDatetime;
    }

    public void setRegistrationDatetime(Timestamp registrationDatetime) {
        this.registrationDatetime = registrationDatetime;
    }

    @Basic
    @Column(name = "LAST_VISIT_DATETIME")
    public Timestamp getLastVisitDatetime() {
        return lastVisitDatetime;
    }

    public void setLastVisitDatetime(Timestamp lastVisitDatetime) {
        this.lastVisitDatetime = lastVisitDatetime;
    }
}
