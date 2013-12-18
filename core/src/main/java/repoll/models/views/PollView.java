package repoll.models.views;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Mikhail Golubev
 */
public interface PollView {
    long getId();

    @Nullable
    UserView getAuthor();

    @NotNull
    Date getCreationDate();

    @NotNull
    String getDescription();

    @NotNull
    String getTitle();
}
