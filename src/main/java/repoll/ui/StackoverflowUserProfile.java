package repoll.ui;

import repoll.service.StackExchangeUser;

import javax.swing.*;
import java.awt.event.*;

public class StackoverflowUserProfile extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel avatarLabel;
    private JLabel rateLabel;
    private JLabel ageLabel;
    private JLabel reputationLabel;

    public StackoverflowUserProfile(StackExchangeUser user) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        avatarLabel.setIcon(new ImageIcon(user.getProfileImageLink()));
        avatarLabel.setText("");
        rateLabel.setText(String.valueOf(user.getAcceptRate()));
        ageLabel.setText(String.valueOf(user.getAge()));
        reputationLabel.setText("" + user.getReputation());

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
}
