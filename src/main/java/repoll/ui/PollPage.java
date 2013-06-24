package repoll.ui;

import repoll.core.*;
import repoll.mappers.MapperException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class PollPage {
    private static final Logger LOG = Logger.getLogger(PollPage.class.getName());

    private JPanel rootPanel;
    private JTextPane descriptionPane;
    private JPanel answersPanel;
    private JPanel commentariesPanel;
    private JLabel titleLabel;
    private JButton addCommentaryButton;
    private JPanel resultsPanel;
    private JButton voteButton;
    private JPanel votingPanel;
    private Poll poll;

    public PollPage(final Poll poll) {
        this.poll = poll;

        commentariesPanel.setLayout(new GridLayout(0, 1));
        votingPanel.setLayout(new GridLayout(0, 1));
        resultsPanel.setLayout(new GridLayout(0, 1));

        titleLabel.setText(poll.getTitle());
        descriptionPane.setText(poll.getDescription());
        final ButtonGroup buttonGroup = new ButtonGroup();
        final CardLayout cardLayout = (CardLayout) answersPanel.getLayout();
        final User user = MainApplication.getInstance().getCurrentUser();
        try {
            List<Answer> answers = poll.getAnswers();
            final Map<String, Answer> answersMap = new TreeMap<>();
            for (Answer answer : answers) {
                answersMap.put(answer.getDescription(), answer);
            }
            fillResultsPanel(poll);
            for (Answer answer : answers) {
                JRadioButton button = new JRadioButton(answer.getDescription());
                buttonGroup.add(button);
                votingPanel.add(button);
            }
            voteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    String selectedAnswer = getSelectedRadioButtonTest(buttonGroup);
                    if (selectedAnswer == null) {
                        return;
                    }
                    try {
                        user.voteFor(answersMap.get(selectedAnswer));
                    } catch (MapperException e) {
                        LOG.throwing("voteButton ActionListener", "actionPerformed", e);
                    }
                    resultsPanel.removeAll();
                    fillResultsPanel(poll);
                    resultsPanel.revalidate();
                    cardLayout.show(answersPanel, "Results");
                }
            });
            if (userAlreadyVoted()) {
                cardLayout.show(answersPanel, "Results");
            } else {
                cardLayout.show(answersPanel, "Voting");
            }
            fillCommentariesPanel(poll);
            addCommentaryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    String s = JOptionPane.showInputDialog("Enter commentary");
                    try {
                        user.commentPoll(poll, s);
                    } catch (MapperException e) {
                        LOG.throwing("addCommentaryButton ActionListener", "actionPerformed", e);
                    }
                    commentariesPanel.removeAll();
                    fillCommentariesPanel(poll);
                    commentariesPanel.revalidate();
                }
            });
        } catch (MapperException e) {
            LOG.throwing("PollPage", "PollPage", e);
        }
    }

    private void fillCommentariesPanel(Poll poll) {
        try {
            for (Commentary commentary : poll.getCommentaries()) {
                commentariesPanel.add(new CommentTile(commentary));
            }
        } catch (MapperException e) {
            LOG.throwing("PollPage", "fillCommentariesPanel", e);
        }
    }

    private boolean userAlreadyVoted() {
        User user = MainApplication.getInstance().getCurrentUser();
        // TODO: extract to separate query
        boolean alreadyVoted = false;
        try {
            for (Vote vote : user.getVotes()) {
                if (vote.getAnswer().getPoll().equals(poll)) {
                    alreadyVoted = true;
                }

            }
        } catch (MapperException e) {
            LOG.throwing("PollPage", "userAlreadyVoted", e);
            return true;
        }
        return alreadyVoted;
    }

    private void fillResultsPanel(Poll poll) {
        try {
            for (Answer answer : poll.getAnswers()) {
                resultsPanel.add(new JLabel(String.format("%s: %d", answer.getDescription(), answer.getVotes().size())));
            }
        } catch (MapperException e) {
            LOG.throwing("PollPage", "fillResultsPanel", e);
        }
    }

    private String getSelectedRadioButtonTest(ButtonGroup group) {
        for (AbstractButton button : Collections.list(group.getElements())) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private static class CommentTile extends JPanel {
        private CommentTile(Commentary commentary) {
            super(new GridLayout(2, 0));
            add(new JLabel(commentary.getMessage(), SwingConstants.LEFT));
            String author = commentary.getAuthor() == null ? "Anonymous" : commentary.getAuthor().getLogin();
            add(new JLabel("Commented by: " + author, SwingConstants.RIGHT));
        }
    }
}
