package repoll.server.ui;

import repoll.models.User;
import repoll.server.mappers.MapperException;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class RegistrationDialog extends JDialog {
    private static final Logger LOG = Logger.getLogger(RegistrationDialog.class.getName());

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField userLoginField;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatField;
    private JTextArea aboutField;
    private JTextField stackoverflowIdField;
    private User registeredUser;

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
            registeredUser = createUser();
            if (registeredUser != null) {
                dispose();
            }
        }
    }

    private User createUser() {
        User user = User.builder(userLoginField.getText(), passwordField.getText())
                .firstName(firstNameField.getText())
                .middleName(middleNameField.getText())
                .lastName(lastNameField.getText())
                .additionalInfo(aboutField.getText())
                .build();
        if (!stackoverflowIdField.getText().isEmpty()) {
            user.setStackoverflowId(Integer.parseInt(stackoverflowIdField.getText()));
        }
        try {
            user.insert();
            return user;
        } catch (MapperException e) {
            JOptionPane.showMessageDialog(this, "Can't save user: " + e.getMessage(),
                    "User creation error", JOptionPane.ERROR_MESSAGE);
            LOG.throwing("RegistrationDialog", "createUser", e);
        }
        return null;
    }

    private boolean validateFields() {
        if (!ValidationUtil.validateLoginAndShowDefaultMessageDialog(userLoginField.getText())) {
            return false;
        }
        if (!stackoverflowIdField.getText().matches("\\d*")) {
            JOptionPane.showMessageDialog(this, "Stackoverflow ID should be non-negative number",
                    "Invalid Stackoverflow ID", JOptionPane.ERROR_MESSAGE);
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

    public User getUser() {
        return registeredUser;
    }
}
