package repoll.models.views;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public interface AnswerView {
    long getId();

    @NotNull
    PollView getPoll();

    @NotNull
    String getDescription();
}
