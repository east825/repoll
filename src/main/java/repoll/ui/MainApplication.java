package repoll.ui;

import repoll.core.Poll;
import repoll.core.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainApplication extends JFrame {
    private static MainApplication APPLICATION = null;
    public static synchronized MainApplication getInstance() {
        if (APPLICATION == null) {
            APPLICATION = new MainApplication();
        }
        return APPLICATION;
    }

    private JTextField searchField;
    private JButton searchButton;
    private JButton addPollButton;
    private JPanel contentPanel;
    private JPanel rootPanel;
    private JComponent contentPanelComponent;
    private User currentUser;

    private MainApplication() {
        // empty
    }

    public void switchMainContent(JPanel component) {
        contentPanel.remove(contentPanelComponent);
        contentPanelComponent = component;
        contentPanel.add(contentPanelComponent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void createAndShowGUI() {
        contentPanelComponent = new SearchResults(this);
        contentPanel.add(contentPanelComponent, BorderLayout.CENTER);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Poll> polls = SearchUtil.findPolls(searchField.getText());
                switchMainContent(new SearchResults(polls, MainApplication.this));
            }
        });

        addPollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PollCreationDialog dialog = new PollCreationDialog();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        add(rootPanel);
        setTitle("Repoll UI");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        LoginDialog loginDialog = new LoginDialog();
        loginDialog.pack();
        loginDialog.setVisible(true);
        currentUser = loginDialog.getUser();
        if (currentUser == null) {
            dispose();
        }
    }
}
