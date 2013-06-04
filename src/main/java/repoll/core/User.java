package repoll.core;

import org.jetbrains.annotations.NotNull;

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
    private static String calculatePasswordHash(@NotNull String password) {
        return password;
    }

    @Override
    public String toString() {
        return String.format("User(id=%d, login='%s')", getId(), login);
    }

    @NotNull
    public List<Poll> getAuthoredPolls() {
        return Collections.emptyList();
    }

    @NotNull
    public List<Commentary> getCommentaries() {
        return Collections.emptyList();
    }

    @NotNull
    public List<Vote> getVotes() {
        return Collections.emptyList();
    }

    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    @NotNull
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(@NotNull String middleName) {
        this.middleName = middleName;
    }

    @NotNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull String lastName) {
        this.lastName = lastName;
    }

    @NotNull
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(@NotNull String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    @NotNull
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(@NotNull String password) {
        this.passwordHash = calculatePasswordHash(password);
    }

    @NotNull
    public Date getRegistrationDate() {
        return registrationDate;
    }

    @NotNull
    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(@NotNull Date lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public static class Builder {
        private String firstName = "";
        private String middleName = "";
        private String lastName = "";
        private String additionalInfo = "";
        private String login;
        private String passwordHash;
        private Date registrationDate = new Date();
        private Date lastVisitDate = new Date();

        public Builder(@NotNull String login, @NotNull String password) {
            this.login = login;
            this.passwordHash = calculatePasswordHash(password);
        }

        public Builder firstName(@NotNull String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder middleName(@NotNull String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder lastName(@NotNull String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder additionalInfo(@NotNull String additionalInfo) {
            this.additionalInfo = additionalInfo;
            return this;
        }

        public Builder registrationDate(@NotNull Date registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder lastVisitDate(@NotNull Date lastVisitDate) {
            this.lastVisitDate = lastVisitDate;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
