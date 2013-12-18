package repoll.models.views;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Mikhail Golubev
 */
public interface VoteView {
    long getId();

    @Nullable
    UserView getAuthor();

    @NotNull
    AnswerView getAnswer();

    @NotNull
    Date getCreationDate();
}
