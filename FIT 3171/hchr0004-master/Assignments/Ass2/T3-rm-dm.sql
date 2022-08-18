--****PLEASE ENTER YOUR DETAILS BELOW****
--T3-rm-dm.sql

--Student ID: 32134835
--Student Name: Hazael Frans Christian
--Unit Code: FIT3171
--Applied Class No: 3

/* Comments for your marker:
    This is the file for the task 3 answers.
    
    Some competitor's information like their email and dob was not specified
    in the assignment and since they cannot be null, they are filled with info 
    of what I think is the more appropiate.
*/

--3(a)
drop sequence comp_seq;
drop sequence team_seq;

create sequence comp_seq
    start with 100
    increment by 1
    maxvalue 99999
    nocycle;
    
create sequence team_seq
    start with 100
    increment by 1
    maxvalue 999
    nocycle;

--3(b)
-- Insert emergency contact
insert into emercontact(
    ec_phone,
    ec_fname,
    ec_lname
) values(
    '0476541234',
    'Jack',
    'Kai'
);

-- Insert competitor info and their respective entries
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
    'Daniel',
    'Kai',
    'U',
    to_date('27/May/1995', 'DD/MON/YYYY'),
    'daniel_k@yahoo.com',
    'Y',
    '0472320527',
    'P',
    '0476541234'
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
        eventtype_code = '21K'),
    (select (count(*) + 1) from entry
        where event_id = (
            select event_id from event
                where carn_date = (select carn_date from carnival 
                    where carn_name = 'RM Autumn Series Caulfield 2022') and eventtype_code = '21K'
        )),
    null,
    null,
    comp_seq.currval,
    null,
    (select char_id from charity
        where char_name = 'Beyond Blue')
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
    'Annabelle',
    'Kai',
    'U',
    to_date('17/Sep/1997', 'DD/MON/YYYY'),
    'kainnabelle@gmail.com',
    'Y',
    '0472323408',
    'P',
    '0476541234'
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
        eventtype_code = '21K'),
    (select (count(*) + 1) from entry
        where event_id = (
            select event_id from event
                where carn_date = (select carn_date from carnival where carn_name = 'RM Autumn Series Caulfield 2022') and eventtype_code = '21K'
        )),
    null,
    null,
    comp_seq.currval,
    null,
    (select char_id from charity
        where char_name = 'Amnesty International')
);

--Commit
commit;

--3(c)
--Make the team
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
    'Kai Speedstars',
    (select carn_date from event 
    where event_id = (select event_id from entry 
        where comp_no = (select comp_no from competitor where comp_fname = 'Annabelle' and comp_lname = 'Kai'))),
    1,
    (select event_id from entry
        where comp_no = (select comp_no from competitor where comp_fname = 'Annabelle' and comp_lname = 'Kai')),
    (select entry_no from entry
        where comp_no = (select comp_no from competitor where comp_fname = 'Annabelle' and comp_lname = 'Kai')),
    (select char_id from entry
        where comp_no = (select comp_no from competitor where comp_fname = 'Annabelle' and comp_lname = 'Kai'))
);

--Update Annabelle to have a team_id
Update entry
    set team_id = team_seq.currval
    where comp_no = (select comp_no from competitor where comp_fname = 'Annabelle' and comp_lname = 'Kai');

--Commit
commit;

--3(d)
--Update Daniel's entry
Update entry
    set event_id = (select event_id from event
        where carn_date = (select carn_date from carnival where carn_name = 'RM Autumn Series Caulfield 2022') and
        eventtype_code = '10K')
    where comp_no = (select comp_no from competitor
        where comp_fname = 'Daniel' and comp_lname = 'Kai');

Update entry
    set entry_no = (select (count(*)) from entry
        where event_id = (
            select event_id from event
                where carn_date = (select carn_date from carnival where carn_name = 'RM Autumn Series Caulfield 2022') and eventtype_code = '10K'
        ))
    where comp_no = (select comp_no from competitor
        where comp_fname = 'Daniel' and comp_lname = 'Kai');

Update entry
    set team_id = (select team_id from team
        where team_name = 'Kai Speedstars')
    where comp_no = (select comp_no from competitor
        where comp_fname = 'Daniel' and comp_lname = 'Kai');

Update team
    set team_no_members = (select count(*) from entry 
        where team_id = (select team_id from team 
            where team_name = 'Kai Speedstars'))
    where team_id = (select team_id from team 
        where team_name = 'Kai Speedstars');

--Commit
commit;

--3(e)
--Daniel withdraws
Delete from entry
    where comp_no = (select comp_no from competitor
        where comp_fname = 'Daniel' and comp_lname = 'Kai')
        and event_id = (select ent.event_id from ((event evt join carnival carn on evt.carn_date = carn.carn_date) 
                                        join entry ent on evt.event_id = ent.event_id)
                            where carn_name = 'RM Autumn Series Caulfield 2022'
                            and comp_no = (select comp_no from competitor
                                where comp_fname = 'Daniel' and comp_lname = 'Kai'));

Update team
    set team_no_members = (select count(*) from entry 
        where team_id = (select team_id from team 
            where team_name = 'Kai Speedstars'))
    where team_id = (select team_id from team 
        where team_name = 'Kai Speedstars');
    
--Annabelle disbanded Kai Speedstar
Update entry
    set team_id = null
    where comp_no = (select comp_no from competitor
        where comp_fname = 'Annabelle' and comp_lname = 'Kai');
    
Delete from team
    where team_id = (select team_id from team
        where team_name = 'Kai Speedstars');

Update entry
    set char_id = (select char_id from charity
        where char_name = 'Beyond Blue')
    where comp_no = (select comp_no from competitor
        where comp_fname = 'Annabelle' and comp_lname = 'Kai');
    
--Commit
commit;
