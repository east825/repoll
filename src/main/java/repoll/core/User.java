package repoll.core;

import org.jetbrains.annotations.NotNull;
import repoll.mappers.AbstractMapper;
import repoll.mappers.MapperException;
import repoll.mappers.Mappers;

import java.util.Date;
import java.util.List;

public class User extends DomainObject {
    private String firstName, middleName, lastName;
    private String additionalInfo;
    private String login, passwordHash;
    private Date registrationDate;
    private Date lastVisitDate;
    private int stackoverflowId = -1;

    public User(Builder builder) {
        firstName = builder.firstName;
        middleName = builder.middleName;
        lastName = builder.lastName;
        additionalInfo = builder.additionalInfo;
        login = builder.login;
        passwordHash = builder.passwordHash;
        registrationDate = builder.registrationDate;
        lastVisitDate = builder.lastVisitDate;
        stackoverflowId = builder.stackoverflowId;
    }

    public static User newFromCredentials(String login, String password) throws MapperException {
        User user = new Builder(login, password).build();
        user.insert();
        return user;
    }

    public static Builder builder(String login, String password) {
        return new Builder(login, password);
    }

    public static AbstractMapper<User> getMapper() {
        return Mappers.getForClass(User.class);
    }

    // TODO: calculate actual hash here
    private static String calculatePasswordHash(@NotNull String password) {
        return password;
    }

    public Vote voteFor(Answer answer) throws MapperException {
        Vote vote = new Vote(this, answer);
        vote.insert();
        return vote;
    }

    public Commentary commentPoll(Poll poll, String message) throws MapperException {
        Commentary commentary = new Commentary(this, poll, message);
        commentary.insert();
        return commentary;
    }

    public Poll createPoll(String title) throws MapperException {
        return createPoll(title, "");
    }

    public Poll createPoll(String title, String description) throws MapperException {
        Poll poll = new Poll(this, title, description);
        poll.insert();
        return poll;
    }

    @Override
    public String toString() {
        return String.format("User(id=%d, login='%s')", getId(), login);
    }

    @NotNull
    public List<Poll> getAuthoredPolls() throws MapperException {
        return Mappers.getForClass(Poll.class).selectRelated(this);
    }

    @NotNull
    public List<Commentary> getCommentaries() throws MapperException {
        return Mappers.getForClass(Commentary.class).selectRelated(this);
    }

    @NotNull
    public List<Vote> getVotes() throws MapperException {
        return Mappers.getForClass(Vote.class).selectRelated(this);
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
    public String getPresentableName() {
        if (!firstName.isEmpty()) {
            String name = firstName;
            if (!middleName.isEmpty()) {
                name += " " + middleName;
            }
            if (!lastName.isEmpty()) {
                name += " " + lastName;
            }
            return name;
        }
        return login;
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

    public int getStackoverflowId() {
        return stackoverflowId;
    }

    public void setStackoverflowId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Stackoverflow id should be non-negative number");
        }
        this.stackoverflowId = id;
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
        private int stackoverflowId = -1;

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

        public Builder stackoverflowId(int id) {
            if (id < 0) {
                throw new IllegalArgumentException("Stackoverflow id should be non-negative number");
            }
            stackoverflowId = id;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
