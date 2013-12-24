package repoll.client.ui;

import repoll.models.User;

import javax.swing.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private static final String DEV_USER = "east825";
    private static final String DEV_PASSWORD = "";
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registrationButton;
    private User currentUser;

    public LoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        registrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationDialog dialog = new RegistrationDialog();
                dialog.pack();
                dialog.setVisible(true);
                currentUser = dialog.getUser();
                if (currentUser != null) {
                    dispose();
                }
            }
        });
    }

    private void onOK() {
        if (loginField.getText().isEmpty()) {
            // Testing backdoor
            currentUser = MainApplication.getFacade().findUser(DEV_USER, DEV_PASSWORD);
            if (currentUser == null) {
                throw new AssertionError("Test user 'east825' was not found");
            }
            dispose();
            return;
        }
        if (!validateFields()) {
            return;
        }
        currentUser = MainApplication.getFacade().findUser(loginField.getText(), passwordField.getText());
        if (currentUser == null) {
            JOptionPane.showMessageDialog(contentPane, "Wrong credentials", "Wrong login or password", JOptionPane.ERROR_MESSAGE);
        } else {
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

    private boolean validateFields() {
        return ValidationUtil.validateLoginAndShowDefaultMessageDialog(loginField.getText());
    }

    public User getUser() {
        return currentUser;
    }
}
