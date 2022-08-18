--****PLEASE ENTER YOUR DETAILS BELOW****
--T2-rm-insert.sql

--Student ID: 32134835
--Student Name: Hazael Frans Christian
--Unit Code: FIT3171
--Applied Class No: 3

/* Comments for your marker:
    This is the file for the task 2 answers.
    
    The entry number that is entered for the team entity is the team's leader!
    
    The entry table FKs for team_id has to be dropped when inserting, because the team
    table isn't populated yet when populating the entry table.
*/

-- Task 2 Load the EMERCONTACT, COMPETITOR, ENTRY and TEAM tables with your own
-- test data following the data requirements expressed in the brief

-- =======================================
-- EMERCONTACT
-- =======================================
insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '0123456789',
    'Birdie',
    'Rhode'
);

insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '0376489123',
    'Maggie Armen',
    'Dnooles'
);

insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '0347123787',
    'Nayam',
    'Blumfie'
);

insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '0923105179',
    'AJ',
    null
);

insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '0923123579',
    null,
    'Wijaya'
);

insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '12345',
    null,
    null
);

-- =======================================
-- COMPETITOR
-- =======================================
insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    000,
    'Timi',
    'Wijaya',
    'M',
    to_date('15/Jun/2002', 'DD/MON/YYYY'),
    'imit@hotmail.com',
    'Y',
    '0812398527',
    'P',
    '0923123579'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    001,
    'Halim',
    'Wijaya',
    'M',
    to_date('10/Sep/1998', 'DD/MON/YYYY'),
    'liham@gmail.com',
    'Y',
    '0328304127',
    'P',
    '0923123579'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    002,
    'Midya',
    'Wijaya',
    'F',
    to_date('23/Jan/1999', 'DD/MON/YYYY'),
    'midya_w@gmail.com',
    'N',
    '0239102457',
    'P',
    '0923123579'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    003,
    'Attler',
    'Kanes',
    'F',
    to_date('15/Jul/2002', 'DD/MON/YYYY'),
    'candykanes@gmail.com',
    'N',
    '017239091',
    'G',
    '0123456789'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    004,
    'Der',
    'Danpa',
    'U',
    to_date('07/May/2000', 'DD/MON/YYYY'),
    'pandar__@gmail.com',
    'N',
    '017133123',
    'G',
    '0123456789'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    005,
    'Kad',
    'Calico',
    'F',
    to_date('17/Dec/2000', 'DD/MON/YYYY'),
    'calidoskope@gmail.com',
    'Y',
    '017809809',
    'G',
    '0123456789'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    006,
    'Despad',
    'Armen',
    'M',
    to_date('21/Apr/1972', 'DD/MON/YYYY'),
    'despad_armen1970@gmail.com',
    'Y',
    '011234234',
    'T',
    '0376489123'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    007,
    'Yamsang',
    'Xuto',
    'M',
    to_date('14/Mar/2008', 'DD/MON/YYYY'),
    'xutoxutoxutoxuto@gmail.com',
    'N',
    '014123986',
    'F',
    '0376489123'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    008,
    'Dlom',
    'Schal',
    'M',
    to_date('01/Apr/1990', 'DD/MON/YYYY'),
    'schal_d@gmail.com',
    'N',
    '067123098',
    'F',
    '0347123787'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    009,
    'Mayin',
    'Dolche',
    'M',
    to_date('01/Oct/1990', 'DD/MON/YYYY'),
    'd_mayin@yahoo.com',
    'N',
    '063098123',
    'F',
    '0347123787'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    010,
    'Muffie Throlm',
    'Dolche',
    'M',
    to_date('13/Aug/1995', 'DD/MON/YYYY'),
    'muffiemuff@gmail.com',
    'N',
    '063098329',
    'F',
    '0347123787'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    011,
    'Mally',
    'Lastch',
    'U',
    to_date('03/Jan/1991', 'DD/MON/YYYY'),
    'lastchymalls@gmail.com',
    'N',
    '078105789',
    'F',
    '0347123787'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    012,
    'TJ',
    null,
    'M',
    to_date('11/Feb/2004', 'DD/MON/YYYY'),
    'TJ__________@gmail.com',
    'Y',
    '0927380190',
    'F',
    '0923105179'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    013,
    'CJ',
    null,
    'M',
    to_date('21/Feb/2004', 'DD/MON/YYYY'),
    'CJ239074@gmail.com',
    'Y',
    '0922397508',
    'F',
    '0923105179'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    014,
    'SJ',
    null,
    'M',
    to_date('15/Jan/2004', 'DD/MON/YYYY'),
    'Salur_Jones@gmail.com',
    'Y',
    '0928002341',
    'F',
    '0923105179'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    015,
    null,
    null,
    'U',
    to_date('01/Jan/2022', 'DD/MON/YYYY'),
    '11000110101@gmail.com',
    'N',
    '6789',
    'T',
    '12345'
);

insert into competitor(
    comp_no,
    comp_fname,
    comp_lname,
    comp_gender,
    comp_dob,
    comp_email,
    comp_unistatus,
    comp_phone,
    comp_ec_relationship,
    ec_phone
) values(
    016,
    null,
    null,
    'U',
    to_date('02/Jan/2022', 'DD/MON/YYYY'),
    '10100101@gmail.com',
    'N',
    '101112',
    'T',
    '12345'
);

-- =======================================
-- ENTRY
-- =======================================
alter table entry 
    disable constraint entry_team_id_fk;

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    1,
    1,
    to_date('9:30:40','HH24:MI:SS'),
    to_date('10:44:37','HH24:MI:SS'),
    000,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    3,
    1,
    to_date('9:17:17','HH24:MI:SS'),
    to_date('10:07:13','HH24:MI:SS'),
    000,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    6,
    1,
    to_date('8:34:44','HH24:MI:SS'),
    to_date('8:58:37','HH24:MI:SS'),
    000,
    555,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    2,
    1,
    to_date('8:33:12','HH24:MI:SS'),
    to_date('10:34:27','HH24:MI:SS'),
    001,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    6,
    2,
    to_date('8:32:14','HH24:MI:SS'),
    to_date('9:01:29','HH24:MI:SS'),
    001,
    555,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    10,
    1,
    to_date('8:01:21','HH24:MI:SS'),
    to_date('8:34:59','HH24:MI:SS'),
    001,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    1,
    2,
    to_date('9:30:54','HH24:MI:SS'),
    to_date('10:34:49','HH24:MI:SS'),
    002,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    3,
    2,
    to_date('9:01:24','HH24:MI:SS'),
    to_date('9:43:15','HH24:MI:SS'),
    002,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    6,
    3,
    to_date('8:30:44','HH24:MI:SS'),
    to_date('8:51:07','HH24:MI:SS'),
    002,
    555,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    10,
    2,
    to_date('8:01:34','HH24:MI:SS'),
    to_date('8:41:39','HH24:MI:SS'),
    003,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    4,
    1,
    to_date('8:31:34','HH24:MI:SS'),
    to_date('11:41:39','HH24:MI:SS'),
    003,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    13,
    1,
    to_date('8:42:23','HH24:MI:SS'),
    to_date('11:21:45','HH24:MI:SS'),
    003,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    14,
    1,
    to_date('8:22:32','HH24:MI:SS'),
    to_date('15:31:54','HH24:MI:SS'),
    004,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    2,
    2,
    to_date('10:20:32','HH24:MI:SS'),
    to_date('12:00:17','HH24:MI:SS'),
    004,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    4,
    2,
    to_date('9:21:13','HH24:MI:SS'),
    to_date('11:03:53','HH24:MI:SS'),
    004,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    11,
    1,
    to_date('7:53:12','HH24:MI:SS'),
    to_date('14:27:14','HH24:MI:SS'),
    012,
    123,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    11,
    2,
    to_date('7:53:24','HH24:MI:SS'),
    to_date('14:27:14','HH24:MI:SS'),
    013,
    123,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    11,
    3,
    to_date('7:53:18','HH24:MI:SS'),
    to_date('14:27:14','HH24:MI:SS'),
    014,
    123,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    9,
    1,
    to_date('8:02:01','HH24:MI:SS'),
    to_date('11:00:51','HH24:MI:SS'),
    005,
    null,
    2
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    13,
    2,
    to_date('8:33:11','HH24:MI:SS'),
    to_date('10:00:37','HH24:MI:SS'),
    006,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    13,
    3,
    to_date('8:32:47','HH24:MI:SS'),
    to_date('9:59:13','HH24:MI:SS'),
    007,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    12,
    1,
    to_date('8:45:45','HH24:MI:SS'),
    to_date('9:35:17','HH24:MI:SS'),
    008,
    null,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    8,
    1,
    to_date('8:07:23','HH24:MI:SS'),
    to_date('9:25:37','HH24:MI:SS'),
    009,
    334,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    8,
    2,
    to_date('8:08:02','HH24:MI:SS'),
    to_date('9:34:52','HH24:MI:SS'),
    010,
    334,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    11,
    4,
    to_date('7:45:01','HH24:MI:SS'),
    to_date('7:45:02','HH24:MI:SS'),
    015,
    010,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    11,
    5,
    to_date('7:45:03','HH24:MI:SS'),
    to_date('7:45:04','HH24:MI:SS'),
    016,
    010,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    14,
    2,
    null,
    null,
    012,
    321,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    14,
    3,
    null,
    null,
    013,
    321,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    14,
    4,
    null,
    null,
    014,
    321,
    null
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    14,
    5,
    null,
    null,
    005,
    null,
    2
);

insert into entry(
    event_id,
    entry_no,
    entry_starttime,
    entry_finishtime,
    comp_no,
    team_id,
    char_id
) values(
    14,
    6,
    null,
    null,
    015,
    null,
    2
);

-- =======================================
-- TEAM
-- =======================================
insert into team(
    team_id,
    team_name,
    carn_date,
    team_no_members,
    event_id,
    entry_no,
    char_id
) values(
    321,
    'The J team',
    TO_DATE('29/May/2022', 'DD/MON/YYYY'),
    3,
    14,
    2,
    null
);

insert into team(
    team_id,
    team_name,
    carn_date,
    team_no_members,
    event_id,
    entry_no,
    char_id
) values(
    123,
    'The J team',
    TO_DATE('14/MAR/2022', 'DD/MON/YYYY'),
    3,
    11,
    1,
    null
);

insert into team(
    team_id,
    team_name,
    carn_date,
    team_no_members,
    event_id,
    entry_no,
    char_id
) values(
    334,
    'The Unreal',
    TO_DATE('05/FEB/2022', 'DD/MON/YYYY'),
    2,
    8,
    1,
    null
);

insert into team(
    team_id,
    team_name,
    carn_date,
    team_no_members,
    event_id,
    entry_no,
    char_id
) values(
    555,
    'Midias',
    TO_DATE('05/FEB/2022', 'DD/MON/YYYY'),
    3,
    6,
    1,
    null
);

insert into team(
    team_id,
    team_name,
    carn_date,
    team_no_members,
    event_id,
    entry_no,
    char_id
) values(
    010,
    'Broken',
    TO_DATE('14/MAR/2022', 'DD/MON/YYYY'),
    2,
    11,
    4,
    null
);

--Rebuild any dropped constraints
alter table entry
    enable constraint entry_team_id_fk;
        
commit;