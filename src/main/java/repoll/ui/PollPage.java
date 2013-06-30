package repoll.ui;

import repoll.core.*;
import repoll.mappers.MapperException;
import repoll.service.StackExchangeUser;

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
    private JPanel answersPanel;
    private JPanel commentsPanel;
    private JLabel descriptionLabel;
    private JButton addCommentaryButton;
    private JPanel resultsPanel;
    private JButton voteButton;
    private JPanel votingPanel;
    private JButton viewProfileButton;
    private JList<Commentary> commentsList;
    private Poll poll;

    public PollPage(final Poll poll) {
        this.poll = poll;

        votingPanel.setLayout(new GridLayout(0, 1));
        resultsPanel.setLayout(new GridLayout(0, 1));

        commentsList.setCellRenderer(new ListCellRenderer<Commentary>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Commentary> list, Commentary value, int index, boolean isSelected, boolean cellHasFocus) {
                return new CommentTile(value);
            }
        });

        final User author = poll.getAuthor();
        String userName = author == null? "Anonymous" : author.getPresentableName();
        String description = poll.getDescription().isEmpty()? "No description available" : poll.getDescription();
        descriptionLabel.setText(
                String.format(
                        "<html>" +
                        "<h1>%1$s</h1>" +
                        "<h5><i>Asked by %2$s at %3$tD %3$tT<i><h5>" +
                        "<p>%4$s</p>" +
                        "</html>",
                        poll.getTitle(), userName, poll.getCreationDate(), description)
        );
        final ButtonGroup buttonGroup = new ButtonGroup();
        final CardLayout cardLayout = (CardLayout) answersPanel.getLayout();
        final User user = MainApplication.getInstance().getCurrentUser();
        try {
            List<Answer> answers = poll.getAnswers();
            final Map<String, Answer> answersMap = new TreeMap<>();
            for (Answer answer : answers) {
                answersMap.put(answer.getDescription(), answer);
            }
            resultsPanel.add(new PollResultsVisualization(poll.getAnswers()).getRootPanel());
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
//                    resultsPanel.removeAll();
//                    fillResultsPanel(poll);
                    resultsPanel.revalidate();
                    cardLayout.show(answersPanel, "Results");
                }
            });
            if (userAlreadyVoted()) {
                cardLayout.show(answersPanel, "Results");
            } else {
                cardLayout.show(answersPanel, "Voting");
            }
            fillCommentariesList(poll);
            addCommentaryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    String s = JOptionPane.showInputDialog("Enter commentary");
                    try {
                        user.commentPoll(poll, s);
                    } catch (MapperException e) {
                        LOG.throwing("addCommentaryButton ActionListener", "actionPerformed", e);
                    }
                    fillCommentariesList(poll);
                    commentsPanel.revalidate();
                }
            });
        } catch (MapperException e) {
            LOG.throwing("PollPage", "PollPage", e);
        }
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<StackExchangeUser, Object>() {
                    @Override
                    protected StackExchangeUser doInBackground() throws Exception {
                        if (author == null || author.getStackoverflowId() == -1) {
                            return null;
                        }
                        return StackExchangeUser.loadById(author.getStackoverflowId());
                    }

                    @Override
                    protected void done() {
                        try {
                            StackExchangeUser user = get();
                            if (user != null) {
                                JDialog dialog = new StackoverflowUserProfileDialog(user);
                                dialog.pack();
                                dialog.setModal(false);
                                dialog.setVisible(true);
                            }
                        } catch (Exception e1) {
                            LOG.finest("Error while loading user info: " + e1.getMessage());
                        }
                    }
                }.execute();
            }
        });
    }

    private void fillCommentariesList(Poll poll) {
        DefaultListModel<Commentary> listModel = (DefaultListModel<Commentary>)commentsList.getModel();
        try {
            listModel.clear();
            for (Commentary commentary : poll.getCommentaries()) {
                listModel.addElement(commentary);
            }
        } catch (MapperException e) {
            LOG.throwing("PollPage", "fillCommentariesList", e);
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

    private void createUIComponents() {
        commentsList = new JList<>(new DefaultListModel<Commentary>());
    }

    private static class CommentTile extends JPanel {
        private CommentTile(Commentary commentary) {
            super(new GridLayout(2, 0));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            add(new JLabel(commentary.getMessage(), SwingConstants.LEFT));
            String author = commentary.getAuthor() == null ? "Anonymous" : commentary.getAuthor().getLogin();
            add(new JLabel("Commented by: " + author, SwingConstants.RIGHT));
        }
    }
}
