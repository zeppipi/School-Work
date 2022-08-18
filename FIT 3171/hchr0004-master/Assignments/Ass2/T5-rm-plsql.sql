--****PLEASE ENTER YOUR DETAILS BELOW****
--T5-rm-alter.sql

--Student ID: 32134835
--Student Name: Hazael Frans Christian
--Unit Code: FIT3171
--Applied Class No: 3

/* Comments for your marker:
    This is the file for the task 5 answers.
*/

--5(a)
--Turn on 'serveroutput' since we are working with PLSQL
set serveroutput on;

--Drop newly made trigger
drop trigger add_entry_check;

--Create trigger
create or replace trigger add_entry_check
    before insert on entry
    for each row
    declare count_records number;
    begin
        select count(*) into count_records from ((entry ent join event evt on ent.event_id = evt.event_id) join carnival carn on evt.carn_date = carn.carn_date)
            where ent.comp_no = :new.comp_no and carn.carn_date = (select carn_date from event where event_id = :new.event_id);
        
        if(count_records > 0) then
            raise_application_error(-20000, 'Each entry can only join one carnival');
        else
            dbms_output.put_line('Record successfully inserted');
        end if;
    end;
/

-- Test Harness for 5(a) --
--Prior state
select * from entry;

--Test trigger, a competitor applied for a carnival they weren't in 
--comp_no 000 had joined RM Spring Series Clayton 2021 and RM Spring Series Caulfield 2021
--Trigger shouldn't run
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
    99,
    to_date('9:17:17','HH24:MI:SS'),
    to_date('10:07:13','HH24:MI:SS'),
    000,
    null,
    null
);

--Post state, the inserted value should be successfully inserted
select * from entry;

--Test trigger, a competitor applied for a carnival they were in
--comp no 004 had join RM Autumn Series Caulfield 2022
--Trigger should run
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
    99,
    to_date('8:22:32','HH24:MI:SS'),
    to_date('15:31:54','HH24:MI:SS'),
    004,
    null,
    null
);

--Post state, the inserted value should be unsuccessfully inserted
select * from entry;

--Rollback
rollback;

--5(b)
--Drop newly made columns
alter table eventtype
    drop column eventtype_record;
alter table eventtype
    drop column eventtype_recordholder;
    
--Make new column, currently only filled with nulls
alter table eventtype
    add eventtype_record number(6,2);
alter table eventtype
    add eventtype_recordholder number(3);

update eventtype
    set eventtype_record = (select min(time_elapsed) 
        from ((eventtype evtp join event evt on evtp.eventtype_code = evt.eventtype_code) join entry ent on evt.event_id = ent.event_id)
            where evtp.eventtype_code = eventtype.eventtype_code);
            
update eventtype
    set eventtype_recordholder = (select min(comp_no) 
        from ((eventtype evtp join event evt on evtp.eventtype_code = evt.eventtype_code) join entry ent on evt.event_id = ent.event_id)
            where ent.time_elapsed = eventtype.eventtype_record and evtp.eventtype_code = eventtype.eventtype_code);

--Commit new columns
commit;

--Drop newly made triggers
drop trigger eventtype_new_record;

--Create trigger
create or replace trigger eventtype_new_record
    after insert on entry
    for each row
    declare previous_record number (6,2);
    time_elapsed number(6,2);
    begin
        if(:new.entry_starttime is not null and :new.entry_finishtime is not null) then
            time_elapsed := ((to_char(:new.entry_finishtime, 'HH24') * 60) + (to_char(:new.entry_finishtime, 'SS') / 60) + (to_char(:new.entry_finishtime, 'MI'))) - 
                ((to_char(:new.entry_starttime, 'HH24') * 60) + (to_char(:new.entry_starttime, 'SS') / 60) + (to_char(:new.entry_starttime, 'MI')));
        end if;
    
        select eventtype_record into previous_record 
            from eventtype where eventtype_code = (select eventtype_code from event where event.event_id = :new.event_id);
        
        if (time_elapsed < previous_record) then
            update eventtype
                set eventtype_record = time_elapsed
                    where eventtype_code = (select eventtype_code from event where event_id = :new.event_id);
            
            update eventtype
                set eventtype_recordholder = :new.comp_no
                    where eventtype_code = (select eventtype_code from event where event_id = :new.event_id);
            
            dbms_output.put_line('New Record!');    
        elsif(previous_record is null) then
            update eventtype
                set eventtype_record = time_elapsed
                    where eventtype_code = (select eventtype_code from event where event_id = :new.event_id);
            
            update eventtype
                set eventtype_recordholder = :new.comp_no
                    where eventtype_code = (select eventtype_code from event where event_id = :new.event_id);
            
            dbms_output.put_line('New Record!');
        else
            dbms_output.put_line('Record added with no new record');
        end if;
    end;
/
                
-- Test harness for 5(b)
--Prior state
select * from eventtype;

--Test trigger, an entry has set a new record 
--Using comp no 0, and making them do a new record for the 10K
--Trigger should return 'New Record'
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
    22,
    to_date('8:33:11','HH24:MI:SS'),
    to_date('8:34:11','HH24:MI:SS'),
    000,
    null,
    null
);

--Post state, the eventtype table should be updates
select * from eventtype;

--Rollback
rollback;

--Prior State
select * from entry;
select * from eventtype;

--Test trigger, a new entry was entered that did not make any new records
--Using comp no 0, joined a 10K run and didn't break any records
--Trigger should return 'Record added with no new record'
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
    22,
    to_date('8:33:11','HH24:MI:SS'),
    to_date('20:34:11','HH24:MI:SS'),
    000,
    null,
    null
);

--Post state, eventtype should not be editted and the new entry should be added
select * from entry;
select * from eventtype;

--Rollback
rollback;

--Prior state
select * from eventtype;

--Test trigger, a new entry was added to a event that has no records yet 
--Using comp no 15, joined a 3k community walk that has no records yet
--Trigger should return 'New Record'
update eventtype
    set eventtype_record = null
        where eventtype_code = '3K';
update eventtype
    set eventtype_recordholder = null
        where eventtype_code = '3K';
        
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
    22,
    to_date('8:33:11','HH24:MI:SS'),
    to_date('20:34:11','HH24:MI:SS'),
    015,
    null,
    null
);

--Post state, the eventtype should be editted
select * from eventtype;

--Rollback
rollback;

--Prior State
select * from entry;
select * from eventtype;

--Test trigger, a new entry that was only added for registration was added, has their start and finish time as null
--Using comp no 15, joined a 3K walk but has no records yet
--Trigger should return 'Record added with no new record'
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
    22,
    null,
    null,
    015,
    null,
    null
);

--Post state, the eventtype table should not be edited and the new entry should be added
select * from entry;
select * from eventtype;

--Rollback
rollback;

--5(c)
--Drop newly made procedure
drop procedure event_registration;

--Create new procedure
create or replace procedure event_registration(
        in_comp_no          in number,
        in_carn_date        in date,
        in_eventtype_desc   in varchar2,
        in_team_name        in varchar,
        out_message         out varchar2
    ) as
        var_event_id_found  number(6);
        var_event_id_count  number;
        var_eventtype_code  char(3);
        var_team_found      number;
        var_team_id         number(3);
    begin
        select eventtype_code into var_eventtype_code from eventtype
            where eventtype_desc = in_eventtype_desc;
        
        select count(*) into var_event_id_count from event
            where carn_date = in_carn_date and eventtype_code = var_eventtype_code;
                
        if(var_event_id_count > 0) then
            select event_id into var_event_id_found from event
                where carn_date = in_carn_date and eventtype_code = var_eventtype_code;
            
            insert into entry(
                event_id,
                entry_no,
                entry_starttime,
                entry_finishtime,
                comp_no,
                team_id,
                char_id
            ) values(
                var_event_id_found,
                (select (count(*) + 1) from entry
                    where event_id = (select event_id from event
                        where carn_date = in_carn_date and eventtype_code = var_eventtype_code)),
                null,
                null,
                in_comp_no,
                null,
                null
            );
            
            if(in_team_name is not null) then
                select count(*) into var_team_found from team
                    where team_name = in_team_name;
            
                if(var_team_found  > 0) then
                    select team_id into var_team_id from team
                        where team_name = in_team_name;
                    
                    update entry 
                        set team_id = var_team_id
                            where event_id = var_event_id_found
                            and entry_no = (select count(*) from entry
                                where event_id = (select event_id from event
                                    where carn_date = in_carn_date and eventtype_code = var_eventtype_code));
                    
                    Update team
                        set team_no_members = (select count(*) from entry 
                            where team_id = (select team_id from team 
                                where team_name = in_team_name))
                        where team_id = var_team_id;
                else
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
                        in_team_name,
                        in_carn_date,
                        1,
                        (select event_id from event
                            where carn_date = in_carn_date and eventtype_code = var_eventtype_code),
                        (select count(*) from entry
                            where event_id = (select event_id from event
                                where carn_date = in_carn_date and eventtype_code = var_eventtype_code)),
                        null
                    );
                
                    select team_id into var_team_id from team
                        where team_name = in_team_name;
                    
                    update entry 
                        set team_id = var_team_id
                            where event_id = var_event_id_found 
                            and entry_no = (select count(*) from entry
                                                where event_id = (select event_id from event
                                                    where carn_date = in_carn_date and eventtype_code = var_eventtype_code))
                            and comp_no = in_comp_no;
                end if;
            end if;
            
            out_message := 'Registration Successful';
        else
            out_message := 'Registration Unsuccessful';
        end if;
    end event_registration;
/

-- Test Harness for 5(c)
--Prior state
select * from entry;
select * from team;

--Test Procedure, a valid entry was made and they have no team
--Using the comp no 000, should return successfully
declare
    output varchar2(200);
begin
    event_registration(0, TO_DATE('29/May/2022', 'DD/MON/YYYY'), '5 Km Run', null, output);
    dbms_output.put_line(output);
end;
/

--Post state, a new entry should be made and no new team should be made
select * from entry;
select * from team;

--Rollback
rollback;

--Prior state
select * from entry;
select * from team;

--Test Procedure, a valid entry was made and they have an existing team
--Using comp no 0 and the team name 'Broken', should return successfully
declare
    output varchar2(200);
begin
    event_registration(0, TO_DATE('14/Mar/2022', 'DD/MON/YYYY'), '42.2 Km Marathon', 'Broken', output);
    dbms_output.put_line(output);
end;
/

--Post state, a new entry should be made and it has a team id with it and no new teams should be made
select * from entry;
select * from team;

--Rollback
rollback;

--Prior state
select * from entry;
select * from team;

--Test Procedure, a valid entry was made and they have a new team too
--Using comp no 0 and a team name 'Test', should return successfully
declare
    output varchar2(200);
begin
    event_registration(0, TO_DATE('14/Mar/2022', 'DD/MON/YYYY'), '42.2 Km Marathon', 'Test', output);
    dbms_output.put_line(output);
end;
/

--Post state, a new entry should be made with also a new team
select * from entry;
select * from team;

--Rollback
rollback;

--Prior state
select * from entry;
select * from team;

--Test Procedure, an invalid entry has been entered
--Using comp no 0, should return unsuccessfully
declare
    output varchar2(200);
begin
    event_registration(0, TO_DATE('29/May/2022', 'DD/MON/YYYY'), '3 Km Community Run/Walk', 'Test', output);
    dbms_output.put_line(output);
end;
/

--Post state, nothing should be added in either
select * from entry;
select * from team;

--Rollback
rollback;
