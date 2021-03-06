CREATE TABLE "User" (
    id BIGINT NOT NULL
       CONSTRAINT "User_PK" PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    login VARCHAR(20) NOT NULL CONSTRAINT "User_login_UQ" UNIQUE,
    password VARCHAR(30) NOT NULL,
    firstName VARCHAR(30) NOT NULL DEFAULT '',
    lastName VARCHAR(30) NOT NULL DEFAULT '',
    middleName VARCHAR(30) NOT NULL DEFAULT '',
    additionalInfo VARCHAR(3000) NOT NULL DEFAULT '',
    email VARCHAR(50) NOT NULL CONSTRAINT "User_email_UQ" UNIQUE ,
    dateOfBirth DATE,
    registrationDatetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lastVisitDatetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Poll (
    id BIGINT NOT NULL
        CONSTRAINT "Poll_PK" PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    userId BIGINT
        CONSTRAINT "Poll_User_FK" REFERENCES "User" ON DELETE SET NULL,
    title VARCHAR(100) NOT NULL CONSTRAINT "Poll_title_UQ" UNIQUE CHECK (LENGTH(TRIM(title)) > 0),
    description VARCHAR(3000) NOT NULL DEFAULT '',
    creationDatetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Answer (
    id BIGINT NOT NULL
        CONSTRAINT "Answer_PK" PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    pollId BIGINT NOT NULL
        CONSTRAINT "Answer_Poll_FK" REFERENCES Poll ON DELETE CASCADE,
    -- description field is mandatory
    description VARCHAR(100) NOT NULL,
    -- unique answers for each poll
    CONSTRAINT "Answer_UQ" UNIQUE (pollId, description)
);

CREATE TABLE Vote (
    id BIGINT NOT NULL
        CONSTRAINT "Vote_PK" PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    answerId BIGINT NOT NULL
        CONSTRAINT "Vote_Answer_FK" REFERENCES Answer ON DELETE CASCADE,
    -- votes remain intouch if user no longer exists
    userId BIGINT
        constraint "Vote_User_FK" REFERENCES "User" ON DELETE SET NULL,
    creationDatetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    --  CONSTRAINT Vote_PK PRIMARY KEY (answer_id, user_id)
);

CREATE TABLE Commentary (
    id BIGINT NOT NULL
        CONSTRAINT "Commentary_PK" PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    pollId BIGINT NOT NULL
        CONSTRAINT "Commentary_Poll_FK" REFERENCES Poll ON DELETE CASCADE,
    -- anonymous commentary if user deleted his account
    userId BIGINT
        CONSTRAINT "Commentary_User_FK" REFERENCES "User" ON DELETE SET NULL,
    -- message if mandatory
    message VARCHAR(3000) NOT NULL CHECK (LENGTH(TRIM(message)) > 0),
    creationDatetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
