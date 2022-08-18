SET ECHO ON

SPOOL week7_schema_alter_output.txt

--7.3.1 BASIC INSERTING--
INSERT INTO student VALUES(
11111111,    'Bloggs',      'Fred',        to_date('1-Jan-2003', 'dd-Mon-yyyy')
);
INSERT INTO student VALUES(
11111112,    'Nice',      'Nick',        to_date('10-Oct-2004', 'dd-Mon-yyyy')
);
INSERT INTO student VALUES(
11111113,    'Wheat',      'Wendy',        to_date('05-May-2005', 'dd-Mon-yyyy')
);
INSERT INTO student VALUES(
11111114,    'Sheen',      'Cindy',        to_date('25-Dec-2004', 'dd-Mon-yyyy')
);

INSERT INTO unit VALUES(
'FIT9132',     'Introduction to Databases'
);
INSERT INTO unit VALUES(
'FIT9999',     'FIT Last Unit'
);
INSERT INTO unit VALUES(
'FIT9161',     'Project'
);
INSERT INTO unit VALUES(
'FIT5111',     'Student''s Life'
);

INSERT INTO enrolment VALUES(
'11111111',    'FIT9132',     2020,        1,               35,          'N'
);
INSERT INTO enrolment VALUES(
'11111111',    'FIT9132',     2021,        1,              null,         null   
);
INSERT INTO enrolment VALUES(
'11111112',    'FIT9132',     2020,        2,              83,         'HD'   
);
INSERT INTO enrolment VALUES(
'11111112',    'FIT9161',     2020,        2,              79,         'D'   
);
INSERT INTO enrolment VALUES(
'11111113',    'FIT5111',     2021,        1,              null,         null   
);
INSERT INTO enrolment VALUES(
'11111114',    'FIT5111',     2021,        1,              null,         null   
);

COMMIT;

--7.3.2 Using SEQUENCEs in an INSERT statement--
drop sequence student_seq;
create sequence student_seq start with 11111115 increment by 1;

select * from cat;

INSERT INTO student VALUES(
student_seq.nextval,    'Mickey',      'Mouse',        to_date('1-Jan-1', 'dd-Mon-yyyy')
);

INSERT INTO enrolment VALUES(
student_seq.currval,    'FIT9132',     2020,        1,               null,          null
);

COMMIT;

--7.3.3 Advanced INSERT--
INSERT INTO student VALUES(
'22222222',     'Bob',      'Bobby',    to_date('1-April-2001', 'dd-Mon-yyyy')
);

INSERT INTO enrolment VALUES(
'22222222', (select unit_code from unit where unit_name = 'Introduction to Databases'),     2020,   1,  null,   null
);

--7.3.4 Creating a table and inserting data as a single SQL statement--
drop table FIT9132_STUDENT;
create table FIT9132_STUDENT as select * from enrolment where unit_code = 'FIT9132';
select * from FIT9132_STUDENT;

drop table FIT5111_STUDENT;
create table FIT5111_STUDENT as select * from enrolment where unit_code = 'FIT5111';
select * from FIT5111_STUDENT

COMMIT;

SPOOL OFF

SET ECHO OFF