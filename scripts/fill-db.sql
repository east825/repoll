insert into "User" (login, password, registration_datetime, last_visit_datetime) values 
    ('east825', '7&^%*&^b', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Amanda34', 'sdjflkn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('bill-g', 'JHDfjfn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('sue.js', '7&sdf^b', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into "Poll" (user_id, title, description, creation_datetime) values (
    (select id from "User" where login = 'east825'),
    'How late do you usually go to bed?', 
    'i''ve noticed that last month i usually go to bed rather late. What''s about you?',
    CURRENT_TIMESTAMP);

insert into "Answer" (poll_id, description) values 
    ((select id from "Poll" where title = 'How late do you usually go to bed?'), 'About 10:00 pm'),
    ((select id from "Poll" where title = 'How late do you usually go to bed?'), 'At midnight'),
    ((select id from "Poll" where title = 'How late do you usually go to bed?'), 'After midnight'),
    ((select id from "Poll" where title = 'How late do you usually go to bed?'), 'i don''t sleep at all!');

insert into "Commentary" (poll_id, user_id, message) values 
    (
        (select id from "Poll" where title = 'How late do you usually go to bed?'),
        (select id from "User" where login = 'Amanda34'),
        'What a silly question!'
    ),
    (
        (select id from "Poll" where title = 'How late do you usually go to bed?'),
        (select id from "User" where login = 'bill-g'),
        'i would take a nap too...'
    ),
    (
        (select id from "Poll" where title = 'How late do you usually go to bed?'),
        (select id from "User" where login = 'sue.js'),
        'Now you should ask when do you wake up :)'
    );

insert into "Vote" (answer_id, user_id) values
    (
        (select id from "Answer" 
            where 
                poll_id = (select id from "Poll" where title = 'How late do you usually go to bed?') and
                description = 'About 10:00 pm'),
        (select id from "User" where login = 'Amanda34')
    ),
    (
        (select id from "Answer" 
            where 
                poll_id = (select id from "Poll" where title = 'How late do you usually go to bed?') and
                description = 'At midnight'),
        (select id from "User" where login = 'sue.js')
    ),
    (
        (select id from "Answer" 
            where 
                poll_id = (select id from "Poll" where title = 'How late do you usually go to bed?') and
                description = 'After midnight'),
        (select id from "User" where login = 'bill-g')
    );
