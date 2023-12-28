insert into users(EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE, IS_DELETED) values('test@test.test', '$2a$10$aWqY0MzLKnt.6bvFk4zhPu.HZDabDQttLC2uAupM1yq1p6cTSTjSi', '테스트', 'ROLE_ADMIN', CURRENT_TIMESTAMP(), 'blank-profile-picture.png', false);
insert into users(EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE, IS_DELETED) values('test2@test.test', 'test2', '테스트2', 'ROLE_USER', CURRENT_TIMESTAMP(), 'blank-profile-picture.png', false);
insert into users(EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE, IS_DELETED) values('test3@test.test', 'test3', '테스트3', 'ROLE_USER', CURRENT_TIMESTAMP(), 'blank-profile-picture.png', true);

insert into free_board(WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, IS_DELETED) values('1', 'test', 'test', 0, '2018-07-16 15:55:20.879', '2018-07-16 15:55:20.879', false);
insert into free_board(WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, IS_DELETED) values('1', 'test2', 'test2', 0, '2018-07-17 15:55:20.879', '2018-07-17 15:55:20.879', false);
insert into free_board(WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, IS_DELETED) values('1', 'test3', 'test3', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false);
insert into free_board(WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, IS_DELETED) values('2', 'testtest', 'testtest', 0,  CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false);

insert into free_board_answer(WRITER_ID, FREE_BOARD_ID, CONTENTS, CREATE_DATE, UPDATE_DATE) values('1', '1', 'testtest', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('1', 'FREE_BOARD', '1', '1', '1', '2018-07-16 15:55:20.879');
-- insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('2', 'FREE_BOARD', '2', '2', '1', '2018-07-16 15:55:20.879');
-- insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('3', 'FREE_BOARD', '3', '3', '1', '2018-07-16 15:55:20.879');
-- insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('4', 'FREE_BOARD', '4', '4', '2', '2018-07-16 15:55:20.879');
