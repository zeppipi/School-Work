/*
Databases Week 8 Tutorial
week8_sqlbasic_part_b.sql

student id: 
student name:
last modified date:
*/

/* B1. List all the unit codes, semesters and name of chief examiners 
(ie. the staff who is responsible for the unit) for all the units 
that are offered in 2021. Order the output by semester then by unit code..*/
SELECT unitcode, ofsemester, staffid
FROM uni.offering   s
WHERE to_char(s.ofyear, 'yyyy') = '2021'
ORDER BY ofsemester, s.unitcode;


/* B2. List all the unit codes and the unit names and their year and semester offerings.
Order the output by unit code then by offering year and semester. */
SELECT u.unitcode,u.unitname,to_char(o.ofyear,'yyyy'),o.ofsemester
FROM uni.unit u JOIN uni.offering o ON u.unitcode = o.unitcode
ORDER BY ofyear, ofsemester;


/*
B3. List the student id, student name (firstname and surname) as one attribute 
and the unit name of all enrolments for semester 1 of 2021. 
Order the output by unit name, within a given unit name, order by student id.
*/
SELECT 
    s.stuid, 
    s.stuname, 
    e.unitname
FROM 
    uni.student    s
    JOIN uni.enrolment    e ON s.stuid = e.stuid
WHERE
    e.ofsemester = 1 and e.ofyear = 2021
ORDER BY
    e.unitname, s.stuid;


/* B4. List the unit code, semester, class type (lecture or tutorial), day, time 
and duration (in minutes) for all units taught by Windham Ellard in 2021.
Sort the list according to the unit code, within a given unit code, order by offering semester*/
SELECT
    unitcode, ofsemester, cltype, clday, cltime, clduration
FROM
    uni.schedclass     u1
    JOIN uni.staff   p ON u1.staffid = p.staffid
WHERE
    p.stafffname = 'Windham' AND
    p.stafflname = 'Ellard' AND
    to_char(u1.ofyear) = '2021'
ORDER BY
    unitcode,
    u1.ofsemester;


/* B5. Create a study statement for Brier Kilgour.
A study statement contains unit code, unit name, semester and year study was attempted,
the mark and grade. If the mark and/or grade is unknown, show the mark and/or grade as ‘N/A’.
Sort the list by year, then by semester and unit code. */


/* B6. List the unit code and unit name of the prerequisite units
of 'Introduction to data science' unit.
Order the output by prerequisite unit code. */
SELECT
    u1.*
    ,u2.unitcode
    ,u2.unitname
FROM
    uni.UNIT a JOIN uni.PREREQ b on a.unitcode = b.unitcode   u1
    u1 JOIN uni.UNIT on = unitcode    u2
WHERE
    u1.unitname = 'Introduction to data science';
ORDER BY
    u2.unitcode;


/* B7. Find all students (list their id, firstname and surname)
who have received an HD for FIT2094 unit in semester 2 of 2021.
Sort the list by student id. */


/* B8.	List the student full name, and unit code for those students
who have no mark in any unit in semester 1 of 2021.
Sort the list by student full name. */




