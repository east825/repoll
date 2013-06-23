package repoll.ui;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registrationButton;

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
                RegistrationDialog registrationDialog = new RegistrationDialog();
                registrationDialog.pack();
                registrationDialog.setVisible(true);
            }
        });
    }

    private void onOK() {
        if (!validateFields()) {
            return;
        }
        if (!Arrays.equals(passwordField.getPassword(), "passwd".toCharArray())) {
            JOptionPane.showMessageDialog(contentPane, "Wrong password for user '" + loginField.getText() + "'",
                    "Wrong password", JOptionPane.ERROR_MESSAGE);

        } else {
            dispose();
        }
    }

    private boolean validateFields() {
        return ValidationUtil.validateLoginAndShowDefaultMessageDialog(loginField.getText());
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        LoginDialog dialog = new LoginDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
