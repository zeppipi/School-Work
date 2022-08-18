/*
Databases Week 8 Tutorial
week8_sqlbasic_part_a.sql

student id: 32134835
student name: Hazael Frans Christian
last modified date: 28th April

*/

/* A1. List all units and their details. Order the output by unit code. */

select unitcode,unitname,unitdesc
from uni.unit
order by unitcode;


/* A2. List all students’ details who live in Caulfield. 
Order the output by student first name.*/

select *
from uni.student
where stuaddress = 'Caulfield'
order by stufname;


/* A3. List the student's surname, firstname and address for those students 
who have a surname starting with the letter 'S' and firstname contains the letter 'i'. 
Order the output by student id*/

select stufname, stulname, stuid
from uni.student
where stufname like 'S%'
and stulname like '%i%'
order by stuid;

  
/* A4. Assuming that a unit code is created based on the following rules:
a. The first three letters represent faculty abbreviation, 
   eg. FIT for the Faculty of Information Technology.
b. The first digit of the number following the letter represents the year level. 
   For example, FIT2094 is a unit code from Faculty of IT (FIT) 
   and the number 2 refers to a second year unit.

List the unit details of all first year units in the Faculty of Information Technology. 
Order the output by unit code.*/

select *
from uni.unit
where unitcode like 'FIT1%'
order by unitcode;


/*A5. List the unit code and semester of all units that are offered in 2021. 
Order the output by unit code, and within a given unit code order by semester. 
To complete this question you need to use the Oracle function to_char to convert 
the data type for the year component of the offering date into text. 
For example, to_char(ofyear,'yyyy') - here we are only using the year part of the date.
*/

SELECT
    u.unitcode,
    o.ofsemester
    
FROM
    uni.unit     u
    JOIN uni.offering o ON u.unitcode = o.unitcode

WHERE
    to_char(o.ofyear,'yyyy') = 2021

ORDER BY
    u.unitcode,
    o.ofsemester;



/* A6. List the year, semester, and unit code for all units that were offered 
in either semester 2 of 2019 or semester 2 of 2020. 
Order the output by year and semester then by unit code.*/

SELECT ofyear, ofsemester, unitcode
FROM uni.offering
WHERE (ofsemester = '2' AND to_char(ofyear,'yyyy') = '2019') OR (ofsemester = '2' AND to_char(ofyear, 'yyyy') = '2020')
ORDER BY ofyear, ofsemester, unitcode;


/* A7. List the student id, unit code and mark 
for those students who have failed any unit in semester 2 of 2021. 
Order the output by student id then order by unit code. */

SELECT 
    s.stuid,
    unitcode,
    enrolmark
FROM 
    uni.student s
    JOIN uni.enrolment e ON s.stuid = e.stuid
WHERE
    enrolgrade = 'N' AND ofsemester = 2 
    AND TO_CHAR(ofyear, 'yyyy') = '2021'
ORDER BY
    stuid,unitcode;





