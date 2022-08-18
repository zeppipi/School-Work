--****PLEASE ENTER YOUR DETAILS BELOW****
--T1-rm-schema.sql

--Student ID: 32134835
--Student Name: Hazael Frans Christian
--Unit Code: FIT3171
--Applied Class No: 3

/* Comments for your marker:
    This is the file for the task 1 answers.
    
    The naming of foreign and primary keys aren't specifically told in the
    assignment specifications, so their names are made by what I think are the
    most appropiate.
*/

-- Task 1 Add Create table statements for the Missing TABLES below
-- Ensure all column comments, and constraints (other than FK's)
-- are included. FK constraints are to be added at the end of this script


-- COMPETITOR
create table competitor(
    comp_no                 number(3) not null,
    comp_fname              varchar(30),
    comp_lname              varchar(30),
    comp_gender             char(1) not null,
    comp_dob                date not null,
    comp_email              varchar(50) not null,
    comp_unistatus          char(1) not null,
    comp_phone              char(10) not null,
    comp_ec_relationship    char(1) not null,
    ec_phone                char(10) not null
);

comment on column competitor.comp_no is
    'The competitor''s unique identifier';

comment on column competitor.comp_fname is
    'The competitor''s first name';
    
comment on column competitor.comp_lname is
    'The competitor''s last name';
    
comment on column competitor.comp_gender is
    'The competitor''s gender, shown with a single letter';
    
comment on column competitor.comp_dob is
    'The competitor''s date of birth';
    
comment on column competitor.comp_email is
    'The competitor''s email';
    
comment on column competitor.comp_unistatus is
    'A sign to tell if the competitor is a student or staff of the university';
    
comment on column competitor.comp_phone is
    'The competitor''s phone number';
    
comment on column competitor.comp_ec_relationship is
    'The competitor''s relationship to the emergency contact';
    
comment on column competitor.ec_phone is
    'The emergency contact''s phone number (foreign key)';

alter table competitor add constraint competitor_pk primary key (comp_no);

alter table competitor add constraint chk_comp_gender check (comp_gender in ('M', 'F', 'U'));
alter table competitor add constraint chk_comp_unistatus check (comp_unistatus in ('Y', 'N'));
alter table competitor add constraint chk_comp_ec_relationship check (comp_ec_relationship in ('P', 'G', 'T', 'F'));


-- EMERCONTACT
create table emercontact(
    ec_phone    char(10) not null,
    ec_fname    varchar(30),
    ec_lname    varchar(30)
);

comment on column emercontact.ec_phone is
    'The emergency contact''s phone number';
    
comment on column emercontact.ec_fname is
    'The emergency contact''s first name';

comment on column emercontact.ec_lname is
    'The emergency contact''s last name';
    
alter table emercontact add constraint emercontact_pk primary key (ec_phone);


--ENTRY
create table entry(
    event_id            numeric(6) not null,
    entry_no            numeric(5) not null,
    entry_starttime     date,
    entry_finishtime    date,
    comp_no             numeric(5)  not null,
    team_id             numeric(3),
    char_id             numeric(3)
);

comment on column entry.event_id is
    'The event''s id the entrant is in';
    
comment on column entry.entry_no is
    'The entrant''s unique id';
    
comment on column entry.entry_starttime is
    'The entrant''s start time';
    
comment on column entry.entry_finishtime is
    'The entrant''s finish time';
    
comment on column entry.comp_no is
    'The competitor''s id in this entrant';
    
comment on column entry.team_id is
    'The team''s id in this entrant';
    
comment on column entry.char_id is
    'The charity''s id in this entrant';

alter table entry add constraint entry_pk primary key (event_id, entry_no);


--TEAM
create table team(
    team_id             number(3) not null,
    team_name           varchar(30) not null,
    carn_date           date not null,
    team_no_members     number(2) not null,
    event_id            number(6) not null,
    entry_no            number(5) not null,
    char_id             number(3)
);

comment on column team.team_id is
    'The team''s unique identifier';

comment on column team.team_name is
    'The team''s name';
    
comment on column team.carn_date is
    'The carnival''s date this team is participiating in';
    
comment on column team.team_no_members is
    'The amount of members in this team';
    
comment on column team.event_id is
    'The event this team is participating in';
    
comment on column team.entry_no is 
    'The team''s entry number';
    
comment on column team.char_id is
    'The charity this team is representing';
    
alter table team add constraint team_pk primary key (team_id);
alter table team add constraint team_name_card_uq unique (team_name, carn_date);

-- Add all missing FK Constraints below here
alter table competitor
    add constraint comp_ec_phone_fk foreign key (ec_phone)
        references emercontact (ec_phone);
        
alter table entry
    add constraint entry_event_id_fk foreign key (event_id)
        references event (event_id);
alter table entry
    add constraint entry_comp_no_fk foreign key (comp_no)
        references competitor (comp_no);
alter table entry
    add constraint entry_team_id_fk foreign key (team_id)
        references team (team_id);
alter table entry
    add constraint entry_char_id_fk foreign key (char_id)
        references charity (char_id);
        
alter table team
    add constraint team_carn_date_fk foreign key (carn_date)
        references carnival (carn_date);
alter table team
    add constraint team_entry_id_no_fk foreign key (event_id, entry_no)
        references entry (event_id, entry_no);
alter table team
    add constraint team_char_id_fk foreign key (char_id)
        references charity (char_id);

commit;