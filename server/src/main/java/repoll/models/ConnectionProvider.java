package repoll.models;

import org.apache.log4j.Logger;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Registry object holding global connection instance.
 */
public class ConnectionProvider {
    private static final Logger LOG = Logger.getLogger(ConnectionProvider.class);

    private static ConnectionProvider instance = defaultConnectionProvider();

    private static ConnectionProvider defaultConnectionProvider() {
        try {
            return new ConnectionProvider(DriverManager.getConnection("jdbc:derby:db/main"));
//            return new ConnectionProvider(DriverManager.getConnection("jdbc:derby://localhost:1527//home/east825/development/repos/repoll/db/main"));
        } catch (SQLException e) {
            LOG.error("CWD: " + Paths.get(".").toAbsolutePath());
            throw new AssertionError("Default connection can't be established", e);
        }
    }

    public static Connection connection() {
        return instance.getConnection();
    }

    public static void registerConnection(Connection connection) {
        instance = new ConnectionProvider(connection);
    }

    private final Connection connection;

    protected ConnectionProvider(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() {
        return connection;
    }
}
