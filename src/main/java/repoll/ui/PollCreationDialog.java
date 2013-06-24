package repoll.ui;

import repoll.core.Poll;
import repoll.mappers.MapperException;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Logger;

public class PollCreationDialog extends JDialog {
    private static final Logger LOG = Logger.getLogger(PollCreationDialog.class.getName());
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField titleField;
    private JTextArea descriptionText;
    private JList<String> answersList;
    private JButton addAnswerButton;
    private JButton removeAnswerButton;
    private DefaultListModel<String> listModel;
    private Poll createdPoll;

    public PollCreationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        listModel = new DefaultListModel<>();
        answersList.setModel(listModel);
        addAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = JOptionPane.showInputDialog(PollCreationDialog.this, "Enter new answer title");
                if (answer != null && !answer.isEmpty()) {
                    listModel.addElement(answer);
                }
            }
        });

        removeAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.removeElement(answersList.getSelectedValue());
            }
        });

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
            createdPoll = createPoll();
            if (createdPoll != null) {
                dispose();
            }
        }
    }

    private Poll createPoll() {
        Poll poll = new Poll(
                MainApplication.getInstance().getCurrentUser(),
                titleField.getText(),
                descriptionText.getText());
        try {
            poll.insert();
            for (int i = 0; i < listModel.size(); i++) {
                poll.addAnswer(listModel.elementAt(i));
            }
            return poll;
        } catch (MapperException e) {
            JOptionPane.showMessageDialog(this, "Can't save poll: " + e.getMessage(),
                    "Poll creation error", JOptionPane.ERROR_MESSAGE);
            LOG.throwing("PollCreationDialog", "savePoll", e);
        }
        return null;
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private boolean validateFields() {
        if (ValidationUtil.isEmptyOrWhitespace(titleField.getText())) {
            JOptionPane.showMessageDialog(this, "Title field can't be empty", "Invalid title", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        ArrayList<String> answers = Collections.list(listModel.elements());
        if (new HashSet<>(answers).size() != answers.size()) {
            JOptionPane.showMessageDialog(this, "Answers contain duplicate values", "Invalid answers", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
