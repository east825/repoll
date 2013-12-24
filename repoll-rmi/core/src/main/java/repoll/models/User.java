package repoll.models;

import org.jetbrains.annotations.NotNull;
import repoll.util.AuthenticationUtil;

import java.util.Date;

public final class User extends DomainObject {
    static final long serialVersionUID = -7259410422969435768L;

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

    /**
     * Create new user from his/her login and password pair and save it in database
     */
    public static User newFromCredentials(String login, String password) {
        return new Builder(login, password).build();
    }

    public static Builder builder(String login, String password) {
        return new Builder(login, password);
    }

    @Override
    public String toString() {
        return String.format("User(id=%d, login='%s')", getId(), login);
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
        this.passwordHash = AuthenticationUtil.hashPassword(password);
    }

    public boolean authenticate(@NotNull String password) {
        return AuthenticationUtil.checkPassword(password, passwordHash);
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
            this.passwordHash = AuthenticationUtil.hashPassword(password);
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
