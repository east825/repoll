CREATE TABLE "User" (
    id int NOT NULL 
       CONSTRAINT "User_PK" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    login varchar(20) NOT NULL CONSTRAINT "User_login_UQ" UNIQUE,
    password varchar(30) NOT NULL,
    first_name varchar(30) NOT NULL DEFAULT '',
    last_name varchar(30) NOT NULL DEFAULT '',
    middle_name varchar(30) NOT NULL DEFAULT '',
    additional_info varchar(3000) NOT NULL DEFAULT '',
    registration_datetime timestamp NOT NULL,
    last_visit_datetime timestamp NOT NULL
);

CREATE TABLE "Poll" (
    id int NOT NULL 
        CONSTRAINT "Poll_PK" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id int
    -- what happens to polls of deleted user?
        CONSTRAINT "Poll_User_FK" REFERENCES "User" ON DELETE SET NULL,
    title varchar(100) NOT NULL CONSTRAINT "Poll_title_UQ" UNIQUE CHECK (LENGTH(TRIM(title)) > 0),
    description varchar(3000) NOT NULL DEFAULT '',
    creation_datetime timestamp NOT NULL
);

CREATE TABLE "Answer" (
    id int NOT NULL 
        CONSTRAINT "Answer_PK" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    poll_id int NOT NULL 
        CONSTRAINT "Answer_Poll_FK" REFERENCES "Poll" ON DELETE CASCADE,
    -- description field is mandatory
    description varchar(100) NOT NULL,
    -- unique answers for each poll
    CONSTRAINT "Answer_UQ" UNIQUE (poll_id, description)
);

CREATE TABLE "Vote" (
    id int NOT NULL
        CONSTRAINT "Vote_PK" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    answer_id int NOT NULL 
        CONSTRAINT "Vote_Answer_FK" REFERENCES "Answer" ON DELETE CASCADE,
    -- votes remain intouch if user no longer exists
    user_id int
        constraint "Vote_User_FK" REFERENCES "User" ON DELETE SET NULL
    --  CONSTRAINT Vote_PK PRIMARY KEY (answer_id, user_id)
);

CREATE TABLE "Commentary" (
    id int NOT NULL 
        CONSTRAINT "Commentary_PK" PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    poll_id int NOT NULL 
        CONSTRAINT "Commentary_Poll_FK" REFERENCES "Poll" ON DELETE CASCADE,
    -- anonymous commentary if user deleted his account
    user_id int 
        CONSTRAINT "Commentary_User_FK" REFERENCES "User" ON DELETE SET NULL,
    -- message if mandatory
    message varchar(3000) NOT NULL CHECK (LENGTH(TRIM(message)) > 0)
);
