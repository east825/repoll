package repoll.util;

import java.sql.Timestamp;
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
}
