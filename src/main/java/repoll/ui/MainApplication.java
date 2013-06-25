package repoll.ui;

import repoll.core.ConnectionProvider;
import repoll.core.Poll;
import repoll.core.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class MainApplication extends JFrame {
    private static final Logger LOG = Logger.getLogger(MainApplication.class.getName());

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
    private JComponent displayedComponent;
    private User currentUser;

    private MainApplication() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Poll> polls = SearchUtil.findPolls(searchField.getText());
                showInMainPanel(new SearchResults(polls, MainApplication.this));
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                LOG.finest("Main window was closed");
                try {
                    LOG.finest("Database connection will be closed");
                    ConnectionProvider.connection().close();
                } catch (SQLException e) {
                    LOG.fine("Error while closing database connection: " + e.getMessage());
                }
            }
        });
    }

    public void showInMainPanel(JComponent component) {
        contentPanel.remove(displayedComponent);
        displayedComponent = component;
        contentPanel.add(displayedComponent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void createAndShowGUI() {
        displayedComponent = new SearchResults(this);
        contentPanel.add(displayedComponent, BorderLayout.CENTER);
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
