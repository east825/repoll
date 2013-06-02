package repoll.mappers;

import java.sql.Timestamp;
import java.util.Date;

public class Util {
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
