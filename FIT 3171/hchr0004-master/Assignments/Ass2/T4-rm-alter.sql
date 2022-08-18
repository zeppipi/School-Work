--****PLEASE ENTER YOUR DETAILS BELOW****
--T4-rm-alter.sql

--Student ID: 32134835
--Student Name: Hazael Frans Christian
--Unit Code: FIT3171
--Applied Class No: 3

/* Comments for your marker:
    This is the file for the task 4 answers.
    
    In task 4a, the column is given 6 digits with 2 decimal places with the assumption
    that no entry will take more than 9999 minutes to finish their run.
    
    In task 4c, roles are written as, at most, two letters representing the role, 'TK' as time keeper,
    'M' as marshal, 'S' as starter and 'FA' as first aid.
*/

--4(a)
--Drop newly made column
alter table entry
    drop column time_elapsed;

--Make new column, currently only filled with nulls
alter table entry
    add time_elapsed number(6,2);

--Populate the column with their appropiate values
update entry
    set time_elapsed = cast(((to_char(entry_finishtime, 'HH24') * 60) + (to_char(entry_finishtime, 'SS') / 60) + (to_char(entry_finishtime, 'MI'))) - 
        ((to_char(entry_starttime, 'HH24') * 60) + (to_char(entry_starttime, 'SS') / 60) + (to_char(entry_starttime, 'MI'))) as number(6,2));
        
commit;

--4(b)
--Drop newly made tables
drop table team_charity cascade constraints;

--Make new table
create table team_charity(
    team_id         number(3) not null,
    char_id         number(3) not null,
    tmchr_percentage      number(3) not null
);

comment on column team_charity.team_id is
    'The id of the team supporting this charity';
    
comment on column team_charity.char_id is
    'The id of the charity the team is supproting';
    
comment on column team_charity.tmchr_percentage is
    'The percentage of the teams fund they are giving to the charity';

alter table team_charity add constraint team_charity_pk primary key (team_id, char_id);
alter table team_charity add constraint team_charity_number_ck check (tmchr_percentage >= 0 and tmchr_percentage <=100);

alter table team_charity 
    add constraint team_charity_team_id_fk foreign key (team_id)
        references team (team_id);
alter table team_charity
    add constraint team_charity_char_id_fk foreign key (char_id)
        references charity (char_id);

commit;
        
--Some dummy data to test this in action
insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '9999999999',
    null,
    null
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
    comp_seq.nextval,
    'test',
    'test',
    'U',
    to_date('27/May/1995', 'DD/MON/YYYY'),
    'test@test.com',
    'Y',
    '99999999',
    'P',
    '9999999999'
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
    (select event_id from event
        where carn_date = (select carn_date from carnival where carn_name = 'RM Autumn Series Caulfield 2022') and
        eventtype_code = '10K'),
    (select (count(*) + 1) from entry
        where event_id = (
            SELECT event_id FROM event
                WHERE carn_date = (SELECT carn_date FROM carnival WHERE carn_name = 'RM Autumn Series Caulfield 2022') and eventtype_code = '10K'
        )),
    null,
    null,
    comp_seq.currval,
    null,
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
    team_seq.nextval,
    'Super Stars',
    (select carn_date from carnival where carn_name = 'RM Autumn Series Caulfield 2022'),
    1,
    (select event_id from entry
        where comp_no = (select comp_no from competitor where comp_fname = 'test' and comp_lname = 'test')),
    (select entry_no from entry
        where comp_no = (select comp_no from competitor where comp_fname = 'test' and comp_lname = 'test')),
    null
);
  
insert into team_charity(
    team_id,
    char_id,
    tmchr_percentage
) values(
    (select team_id from team where team_name = 'Super Stars'),
    (select char_id from charity where char_name = 'Beyond Blue'),
    70
);

insert into team_charity(
    team_id,
    char_id,
    tmchr_percentage
) values(
    (select team_id from team where team_name = 'Super Stars'),
    (select char_id from charity where char_name = 'Amnesty International'),
    30
);

select * from team_charity;
rollback;

--4(c)
--Drop newly made tables
drop table official cascade constraints;

--Make new table
create table official(
    comp_no     number(3) not null,
    carn_date   date not null,
    off_role        char(2) not null
);

comment on column official.comp_no is
    'The id of the competitor that wants to apply as an official';
    
comment on column official.carn_date is
    'The carnival date the competitor would like to be an official at';
    
comment on column official.off_role is
    'The role the competitor would like to be';
    
alter table official add constraint official_pk primary key (comp_no, carn_date);
alter table official add constraint official_role_ck check (off_role in ('TK', 'M', 'S', 'FA'));

alter table official
    add constraint official_comp_no_fk foreign key (comp_no)
        references competitor (comp_no);
alter table official
    add constraint official_carn_date_fk foreign key (carn_date)
        references carnival (carn_date);

commit;