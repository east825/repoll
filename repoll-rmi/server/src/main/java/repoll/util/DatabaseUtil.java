package repoll.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repoll.server.mappers.ConnectionProvider;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseUtil {
    public static final DateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public static Timestamp dateToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Date timestampToDate(Timestamp timestamp) {
        return timestamp;
    }

    public static java.sql.Date dateToSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static Date sqlDateToDate(java.sql.Date date) {
        return date;
    }

    @NotNull
    public static String formatQueryResult(@NotNull String query) throws Exception {
        Connection connection = ConnectionProvider.connection();
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            int nColumns = result.getMetaData().getColumnCount();
            StringBuilder buffer = new StringBuilder();
            while (result.next()) {
                for (int i = 1; i <= nColumns; i++) {
                    buffer.append(result.getObject(i)).append('\t');
                }
                buffer.setCharAt(buffer.length() - 1, '\n');
            }
            return buffer.toString();
        }
    }

    @Nullable
    public static String getUrl(@NotNull Connection connection) {
        try {
            return connection.getMetaData().getURL();
        } catch (SQLException e) {
            return null;
        }
    }
}
