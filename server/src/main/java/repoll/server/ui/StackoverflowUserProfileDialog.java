package repoll.server.ui;

import repoll.server.service.StackExchangeUser;

import javax.swing.*;
import java.awt.event.*;

public class StackoverflowUserProfileDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel avatarLabel;
    private JLabel rateLabel;
    private JLabel ageLabel;
    private JLabel reputationLabel;

    public StackoverflowUserProfileDialog(StackExchangeUser user) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("User: " + user.getDisplayName());

        avatarLabel.setIcon(new ImageIcon(user.getProfileImageLink()));
        rateLabel.setText(String.valueOf(user.getAcceptRate()));
        ageLabel.setText(String.valueOf(user.getAge()));
        reputationLabel.setText(String.valueOf(user.getReputation()));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    /**
     * Alternative way to create dialog with user info
     * using single JLable with inline markup
     */
    public static JLabel buildLabel(StackExchangeUser user) {
        String template = "<html><table>" +
                "<tr><td><b>Profile image:</b></td><img src=\"%s\"/></tr>" +
                "<tr><td><b>Age:</b></td>%d</tr>" +
                "<tr><td><b>Accept rate:</b></td>%d</tr>" +
                "<tr><td><b>Reputation:</b></td><td>%d</td></tr>" +
                "</table></html>";
        String markup = String.format(template,
                user.getProfileImageLink(),
                user.getAge(),
                user.getReputation(),
                user.getReputation());
        return new JLabel(markup);
    }
}
