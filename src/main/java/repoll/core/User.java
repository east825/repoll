package repoll.core;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class User extends DomainObject {
    private String firstName, middleName, lastName;
    private String additionalInfo;
    private String login, passwordHash;
    private Date registrationDate;
    private Date lastVisitDate;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.passwordHash = calculatePasswordHash(password);
    }

    public User(Builder builder) {
        firstName = builder.firstName;
        middleName = builder.middleName;
        lastName = builder.lastName;
        additionalInfo = builder.additionalInfo;
        login = builder.login;
        passwordHash = builder.passwordHash;
        registrationDate = builder.registrationDate;
        lastVisitDate = builder.lastVisitDate;
    }

    public static Builder builder(String login, String password) {
        return new Builder(login, password);
    }

    // TODO: calculate actual hash here
    private static String calculatePasswordHash(String password) {
        return password;
    }

    @Override
    public String toString() {
        return String.format("User(id=%d, login='%s')", getId(), login);
    }

    public List<Poll> getAuthoredPolls() {
        return Collections.emptyList();
    }

    public List<Commentary> getCommentaries() {
        return Collections.emptyList();
    }

    public List<Vote> getVotes() {
        return Collections.emptyList();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(Date lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

//    public List<Poll> getAuthoredPolls() {
//
//    }

    public static class Builder {
        String firstName = "";
        String middleName = "";
        String lastName = "";
        String additionalInfo = "";
        String login;
        String passwordHash;
        Timestamp registrationDate = new Timestamp(System.currentTimeMillis());
        Timestamp lastVisitDate = new Timestamp(System.currentTimeMillis());

        public Builder(String login, String password) {
            this.login = login;
            this.passwordHash = calculatePasswordHash(password);
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder additionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
            return this;
        }

        public Builder registrationDate(Timestamp registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder lastVisitDate(Timestamp lastVisitDate) {
            this.lastVisitDate = lastVisitDate;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
