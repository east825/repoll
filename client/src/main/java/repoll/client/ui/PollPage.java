package repoll.client.ui;

import repoll.client.rest.StackExchangeUser;
import repoll.models.Answer;
import repoll.models.Commentary;
import repoll.models.Poll;
import repoll.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private JButton editPollButton;
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
        String userName = author == null ? "Anonymous" : author.getPresentableName();
        String description = poll.getDescription().isEmpty() ? "No description available" : poll.getDescription();
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
        final User currentUser = MainApplication.getInstance().getCurrentUser();
        List<Answer> answers = MainApplication.getFacade().getPollAnswers(poll);
        final Map<String, Answer> answersMap = new TreeMap<>();
        for (Answer answer : answers) {
            answersMap.put(answer.getDescription(), answer);
        }
        resultsPanel.add(new PollResultsVisualization(answers).getRootPanel());
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
                MainApplication.getFacade().vote(currentUser, answersMap.get(selectedAnswer));
                resultsPanel.revalidate();
                cardLayout.show(answersPanel, "Results");
            }
        });
        if (MainApplication.getFacade().canVoteIn(currentUser, poll)) {
            cardLayout.show(answersPanel, "Voting");
        } else {
            cardLayout.show(answersPanel, "Results");
        }
        fillCommentariesList(poll);
        addCommentaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String s = JOptionPane.showInputDialog("Enter commentary");
                MainApplication.getFacade().comment(currentUser, poll, s);
                fillCommentariesList(poll);
                commentsPanel.revalidate();
            }
        });
        editPollButton.setVisible(currentUser.equals(poll.getAuthor()));
        editPollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new PollEditDialog(poll);
                dialog.setVisible(true);
                MainApplication.getInstance().showInMainPanel(new PollPage(poll).getRootPanel());
            }
        });
        final JPopupMenu popupMenu = new JPopupMenu();
        final RemoveCommentAction removeComment = new RemoveCommentAction();
        popupMenu.add(removeComment);
        commentsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int index = commentsList.locationToIndex(e.getPoint());
                    Commentary comment = commentsList.getModel().getElementAt(index);
                    User currentUser = MainApplication.getInstance().getCurrentUser();
                    boolean isEnabled = currentUser.equals(poll.getAuthor()) || currentUser.equals(comment.getAuthor());
                    removeComment.setEnabled(isEnabled);
                    removeComment.setComment(comment);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        viewProfileButton.setEnabled(poll.getAuthor() != null && poll.getAuthor().getStackoverflowId() >= 0);
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
                            LOG.finest("Error while loading currentUser info: " + e1.getMessage());
                        }
                    }
                }.execute();
            }
        });
    }

    private void fillCommentariesList(Poll poll) {
        DefaultListModel<Commentary> listModel = (DefaultListModel<Commentary>) commentsList.getModel();
        listModel.clear();
        for (Commentary commentary : MainApplication.getFacade().getPollCommentaries(poll)) {
            listModel.addElement(commentary);
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

    private class RemoveCommentAction extends AbstractAction {
        private RemoveCommentAction() {
            super("Remove comment");
        }

        private Commentary comment;

        private void setComment(Commentary comment) {
            this.comment = comment;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (comment != null) {
                MainApplication.getFacade().delete(comment);
                fillCommentariesList(poll);
            }

        }
    }

}
