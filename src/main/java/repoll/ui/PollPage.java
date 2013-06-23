package repoll.ui;

import repoll.core.Answer;
import repoll.core.Commentary;
import repoll.core.Poll;
import repoll.mappers.MapperException;

import javax.swing.*;
import java.awt.*;

public class PollPage {
    private JPanel rootPanel;
    private JTextPane descriptionPane;
    private JPanel answersPanel;
    private JPanel commentariesPanel;
    private JLabel titleLabel;
    private Poll poll;

    public PollPage(Poll poll) {
        this.poll = poll;

        answersPanel.setLayout(new GridLayout(0, 1));
        commentariesPanel.setLayout(new GridLayout(0, 1));
        titleLabel.setText(poll.getTitle());
        descriptionPane.setText(poll.getDescription());
        try {
            for (Answer answer : poll.getAnswers()) {
                answersPanel.add(new JLabel(String.format("%s: %d", answer.getDescription(), answer.getVotes().size())));
            }
            for (Commentary commentary : poll.getCommentaries()) {
                commentariesPanel.add(new CommentTile(commentary));
            }
        } catch (MapperException e) {
            throw new RuntimeException("Error while creating PollPage");
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private static class CommentTile extends JPanel {
        private CommentTile(Commentary commentary) {
            super(new GridLayout(2, 0));
            add(new JLabel(commentary.getMessage(), SwingConstants.LEFT));
            String author = commentary.getAuthor() == null? "Anonymous" : commentary.getAuthor().getLogin();
            add(new JLabel("Commented by: " + author, SwingConstants.RIGHT));
        }
    }
}
