package repoll.ui;

import repoll.core.Poll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class SearchResults extends JPanel {
    private JList<Poll> pollsList;
    private MainPanel mainPanel;

    public SearchResults(MainPanel panel) {
        new SearchResults(Collections.<Poll>emptyList(), panel);
    }

    public SearchResults(List<Poll> polls, MainPanel panel) {
        super(new BorderLayout());
        mainPanel = panel;
        pollsList = new JList<>();
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Search results"));
        pollsList.setCellRenderer(new ListCellRenderer<Poll>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Poll> list, Poll poll, int index, boolean isSelected, boolean cellHasFocus) {
                if (poll == null) {
                    return new JLabel("No polls found");
                }
                return new PollTile(poll);
            }
        });
        pollsList.setModel(new DefaultComboBoxModel<>(polls.toArray(new Poll[polls.size()])));
        pollsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = pollsList.locationToIndex(e.getPoint());
                Poll selectedPoll = pollsList.getModel().getElementAt(index);
                  mainPanel.switchMainContent(new PollPage(selectedPoll).getRootPanel());
//                JOptionPane.showMessageDialog(pollsList, "Poll " + selectedPoll.getTitle() + " selected");

            }
        });
        add(pollsList, BorderLayout.CENTER);
    }

    public static class PollTile extends JPanel {
        public PollTile(Poll poll) {
            setLayout(new GridLayout(2, 0));
            add(new JLabel(poll.getTitle(), SwingConstants.LEFT));
            String authorName = poll.getAuthor() == null ? "Anonymous" : poll.getAuthor().getLogin();
            add(new JLabel("Created by: " + authorName, SwingConstants.RIGHT));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }
}
