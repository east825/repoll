package repoll.models.views;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * @author Mikhail Golubev
 */
public interface UserView {
    long getId();

    @NotNull
    String getFirstName();

    @NotNull
    String getMiddleName();

    @NotNull
    String getLastName();

    @NotNull
    String getPresentableName();

    @NotNull
    String getAdditionalInfo();

    @NotNull
    String getLogin();

    @NotNull
    String getPasswordHash();

    @NotNull
    Date getRegistrationDate();

    @NotNull
    Date getLastVisitDate();

    int getStackoverflowId();
}
