package repoll.ui;

import repoll.core.Poll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MainPanel {
    private JPanel rootPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel contentPanel;

    public MainPanel() {
        contentPanel = new SearchResults(this);
        rootPanel.add(contentPanel, BorderLayout.CENTER);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Poll> polls = SearchUtil.findPolls(searchField.getText());
                switchMainContent(new SearchResults(polls, MainPanel.this));
            }
        });
    }

    void switchMainContent(JPanel component) {
        rootPanel.remove(contentPanel);
        contentPanel = component;
        rootPanel.add(contentPanel, BorderLayout.CENTER);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
