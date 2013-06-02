package repoll.core;

import java.util.Arrays;
import java.util.List;

public class Statements {
    public static final String CREATE_USER_TABLE = "CREATE TABLE \"User\" (\n" +
            "    id int NOT NULL \n" +
            "       CONSTRAINT \"User_PK\" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
            "    login varchar(20) NOT NULL CONSTRAINT \"User_login_UQ\" UNIQUE,\n" +
            "    password varchar(30) NOT NULL,\n" +
            "    first_name varchar(30) NOT NULL DEFAULT '',\n" +
            "    last_name varchar(30) NOT NULL DEFAULT '',\n" +
            "    middle_name varchar(30) NOT NULL DEFAULT '',\n" +
            "    additional_info varchar(3000) NOT NULL DEFAULT '',\n" +
            "    registration_datetime timestamp NOT NULL,\n" +
            "    last_visit_datetime timestamp NOT NULL\n" +
            ")";

    public static final String CREATE_POLL_TABLE = "CREATE TABLE \"Poll\" (\n" +
            "    id int NOT NULL \n" +
            "        CONSTRAINT \"Poll_PK\" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
            "    user_id int\n" +
            "        CONSTRAINT \"Poll_User_FK\" REFERENCES \"User\" ON DELETE SET NULL,\n" +
            "    title varchar(100) NOT NULL CONSTRAINT \"Poll_title_UQ\" UNIQUE CHECK (LENGTH(TRIM(title)) > 0),\n" +
            "    description varchar(3000) NOT NULL DEFAULT '',\n" +
            "    creation_datetime timestamp NOT NULL\n" +
            ")";

    public static final String CREATE_ANSWER_TABLE = "CREATE TABLE \"Answer\" (\n" +
            "    id int NOT NULL \n" +
            "        CONSTRAINT \"Answer_PK\" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
            "    poll_id int NOT NULL \n" +
            "        CONSTRAINT \"Answer_Poll_FK\" REFERENCES \"Poll\" ON DELETE CASCADE,\n" +
            "    description varchar(100) NOT NULL,\n" +
            "    CONSTRAINT \"Answer_UQ\" UNIQUE (poll_id, description)\n" +
            ")";

    public static final String CREATE_VOTE_TABLE = "CREATE TABLE \"Vote\" (\n" +
            "    id int NOT NULL\n" +
            "        CONSTRAINT \"Vote_PK\" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
            "    answer_id int NOT NULL \n" +
            "        CONSTRAINT \"Vote_Answer_FK\" REFERENCES \"Answer\" ON DELETE CASCADE,\n" +
            "    user_id int\n" +
            "        constraint \"Vote_User_FK\" REFERENCES \"User\" ON DELETE SET NULL,\n" +
            "    creation_datetime timestamp NOT NULL\n" +
            ")";

    public static final String CREATE_COMMENTARY_TABLE = "CREATE TABLE \"Commentary\" (\n" +
            "    id int NOT NULL \n" +
            "        CONSTRAINT \"Commentary_PK\" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
            "    poll_id int NOT NULL \n" +
            "        CONSTRAINT \"Commentary_Poll_FK\" REFERENCES \"Poll\" ON DELETE CASCADE,\n" +
            "    user_id int \n" +
            "        CONSTRAINT \"Commentary_User_FK\" REFERENCES \"User\" ON DELETE SET NULL,\n" +
            "    message varchar(3000) NOT NULL CHECK (LENGTH(TRIM(message)) > 0),\n" +
            "    creation_datetime timestamp NOT NULL\n" +
            ")";

    public static List<String> tableCreationStatements() {
        return Arrays.asList(
                CREATE_USER_TABLE,
                CREATE_POLL_TABLE,
                CREATE_ANSWER_TABLE,
                CREATE_VOTE_TABLE,
                CREATE_COMMENTARY_TABLE
        );
    }

    public static final String DROP_COMMENTARY_TABLE = "drop table \"Commentary\"";
    public static final String DROP_VOTE_TABLE = "drop table \"Vote\"";
    public static final String DROP_ANSWER_TABLE = "drop table \"Answer\"";
    public static final String DROP_POLL_TABLE = "drop table \"Poll\"";
    public static final String DROP_USER_TABLE = "drop table \"User\"";

    public static List<String> tableRemovalStatements() {
        return Arrays.asList(
            DROP_COMMENTARY_TABLE,
            DROP_VOTE_TABLE,
            DROP_ANSWER_TABLE,
            DROP_POLL_TABLE,
            DROP_USER_TABLE
        );
    }

    public static final String CLEAR_USER_TABLE = "delete from \"User\"";
    public static final String CLEAR_POLL_TABLE = "delete from \"Poll\"";
    public static final String CLEAR_ANSWER_TABLE = "delete from \"Commentary\"";
    public static final String CLEAR_VOTE_TABLE = "delete from \"Vote\"";
    public static final String CLEAR_COMMENTARY_TABLE = "delete from \"Answer\"";

    public static List<String > tableCleaningStatements() {
        return Arrays.asList(
            CLEAR_USER_TABLE,
            CLEAR_POLL_TABLE,
            CLEAR_ANSWER_TABLE,
            CLEAR_COMMENTARY_TABLE,
            CLEAR_VOTE_TABLE
        );
    }





}
