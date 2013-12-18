package repoll.models.views;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Mikhail Golubev
 */
public interface CommentaryView {
    long getId();

    @Nullable
    UserView getAuthor();

    @NotNull
    String getMessage();

    @NotNull
    Date getCreationDate();
}
