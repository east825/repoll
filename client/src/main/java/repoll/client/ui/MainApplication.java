package repoll.client.ui;

import org.apache.log4j.Logger;
import repoll.client.rmi.RmiFacadeWrapper;
import repoll.core.rmi.RmiServiceFacade;
import repoll.models.Poll;
import repoll.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class MainApplication extends JFrame {
    private static final Logger LOG = Logger.getLogger(MainApplication.class);
    private static RmiFacadeWrapper FACADE;
    public static synchronized RmiFacadeWrapper getFacade() {
        if (FACADE == null) {
            try {
                System.setSecurityManager(new RMISecurityManager());
                FACADE = new RmiFacadeWrapper((RmiServiceFacade) Naming.lookup(RmiServiceFacade.SERVICE_URL));
            } catch (Exception e) {
                throw new AssertionError("", e);
            }
        }
        return FACADE;

    }

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
        getRootPane().setDefaultButton(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInMainPanel(new SearchResultsPage(FACADE.findPolls(searchField.getText())));
            }
        });

        addPollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PollEditDialog dialog = new PollEditDialog();
                dialog.setVisible(true);
                Poll poll = dialog.getPoll();
                if (poll != null) {
                    showInMainPanel(new PollPage(poll).getRootPanel());
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                // TODO: close RMI connection here somehow
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
        displayedComponent = new SearchResultsPage();
        contentPanel.add(displayedComponent, BorderLayout.CENTER);
        add(rootPanel);
        setTitle("Repoll UI");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 600);
        setVisible(true);
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.pack();
        loginDialog.setVisible(true);
        currentUser = loginDialog.getUser();
        if (currentUser == null) {
            dispose();
            return;
        }
        showInMainPanel(new SearchResultsPage(FACADE.getUserPolls(currentUser)));
    }

    public static void main(String[] args) {
        installExceptionHandler();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                APPLICATION = new MainApplication();
                APPLICATION.createAndShowGUI();
            }
        });
    }

    private static void installExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
        System.setProperty( "sun.awt.exception.handler", MyExceptionHandler.class.getName());
    }

    private static class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Server error", JOptionPane.ERROR_MESSAGE);
            LOG.error("Error occurred on server side", e);
        }
    }
}
