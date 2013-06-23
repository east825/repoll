package repoll.ui;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class RegistrationDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField userLoginField;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatField;

    public RegistrationDialog() {
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
        if (validateFields()) {
            saveUser();
            dispose();
        }
    }

    private void saveUser() {
        // pass
    }

    private boolean validateFields() {
        if (ValidationUtil.validateLoginAndShowDefaultMessageDialog(userLoginField.getText())) {
            return false;
        }
        if (!Arrays.equals(passwordField.getPassword(), passwordRepeatField.getPassword())) {
            JOptionPane.showMessageDialog(contentPane, "Passwords are not the same",
                    "Passwords are not the same", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordRepeatField.setText("");
            return false;
        }
        return true;
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        RegistrationDialog dialog = new RegistrationDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
