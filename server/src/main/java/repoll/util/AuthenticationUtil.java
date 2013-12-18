package repoll.util;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Golubev
 */
public class AuthenticationUtil {
    /**
     * Service class
     */
    private AuthenticationUtil() {
        // empty
    }

    public static boolean checkPassword(@NotNull String password, @NotNull String hash) {
        return hashPassword(password).equals(hash);
    }

    @NotNull
    public static String hashPassword(@NotNull String password) {
        return password;
    }
}
