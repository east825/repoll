package repoll.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Registry object holding global connection instance.
 */
public class ConnectionProvider {
    private static ConnectionProvider instance = getDefaultConnection();

    private static ConnectionProvider getDefaultConnection() {
        try {
            return new ConnectionProvider(DriverManager.getConnection("jdbc:derby:db/main"));
        } catch (SQLException e) {
            throw new AssertionError("Default connection can't be established");
        }
    }

    public static Connection connection() {
        return instance.getConnection();
    }

    public static void registerConnection(Connection connection) {
        instance = new ConnectionProvider(connection);
    }

    private Connection connection;

    public ConnectionProvider(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() {
        return connection;
    }
}
