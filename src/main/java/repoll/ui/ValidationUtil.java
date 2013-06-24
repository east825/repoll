package repoll.ui;

import javax.swing.*;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static final Pattern VALID_USERNAME_REGEX = Pattern.compile("(\\w|-)+");

    public static boolean validateLogin(String login) {
        return VALID_USERNAME_REGEX.matcher(login).matches();
    }

    public static boolean isEmptyOrWhitespace(String s) {
        return s.trim().isEmpty();
    }

    public static boolean validateLoginAndShowDefaultMessageDialog(String login) {
        boolean isValid = validateLogin(login);
        if (!isValid) {
            JOptionPane.showMessageDialog(null,
                    "User name can contain only latin letters, numbers, hyphens and underscores",
                    "Invalid name", JOptionPane.ERROR_MESSAGE);
        }
        return isValid;
    }


}
