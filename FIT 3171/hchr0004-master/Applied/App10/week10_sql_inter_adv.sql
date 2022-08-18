/*
Databases Week 10 Applied Class
week10_sql_inter_adv.sql

student id: 32134835
student name: Hazael Frans Christian
last modified date: 12th May 2022

*/

/* 1. Find the maximum mark for FIT9136 in semester 2, 2019. */
select * from enrolment where unit_code = 'fit9136' and enrol_year = 2019 and enrol_semester = 2
and enrol_mark = (select max(enrol_mark) from enrolment)


/* 2. Find the average mark of FIT2094 in semester 2, 2020.
Show the average mark with two decimal places.
Name the output column as average_mark. */



/* 3. List the average mark for each offering of FIT9136.
In the listing, include the year and semester number.
Sort the result according to the year then the semester.*/



/* 4. Find the number of students enrolled in FIT1045 in the year 2019,
under the following conditions:
      a. Repeat students are counted multiple times in each semester of 2019
      b. Repeat students are only counted once across 2019
*/

-- a. Repeat students are counted multiple times in each semester of 2019



-- b. Repeat students are only counted once across 2019



/* 5. Find the total number of prerequisite units for FIT5145. */




/* 6. Find the total number of prerequisite units for each unit.
In the list, include the unitcode for which the count is applicable.
Order the list by unit code.*/



/*7. Find the total number of students
whose marks are being withheld (grade is recorded as 'WH')
for each unit offered in semester 2 2020.
In the listing include the unit code for which the count is applicable.
Sort the list by descending order of the total number of students
whose marks are being withheld, then by the unitcode*/



/* 8. For each prerequisite unit, calculate how many times
it has been used as a prerequisite (number of times used).
In the listing include the prerequisite unit code,
the prerequisite unit name and the number of times used.
Sort the output by prerequisite unit code. */



/*9. Display unit code and unit name of units
which had at least 2 students who were granted deferred exam
(grade is recorded as 'DEF') in semester 2 2021.
Order the list by unit code.*/



/* 10. Find the unit/s with the highest number of enrolments
for each offering in the year 2019.
Sort the list by semester then by unit code. */




/* 11. Find all students enrolled in FIT3157 in semester 1, 2020
who have scored more than the average mark for FIT3157 in the same offering.
Display the students' name and the mark.
Sort the list in the order of the mark from the highest to the lowest
then in increasing order of student name.*/


    
/* 12.  Find the number of scheduled classes assigned to each staff member 
for each semester in 2019. If the number of classes is 2 then this 
should be labelled as a correct load, more than 2 as an overload 
and less than 2 as an underload. Include the staff id, staff first name, 
staff last name, semester, number of scheduled classes and load in the listing. 
Sort the list by decreasing order of the number of scheduled classes 
and when the number of classes is the same, sort by increasing order 
of staff id then by the semester. */



/* 13. Display the unit code and unit name for units that do not have a prerequisite. 
Order the list in increasing order of unit code. 

There are many approaches that you can take in writing an SQL statement to answer this query. 
You can use the SET OPERATORS, OUTER JOIN and a SUBQUERY. 
Write SQL statements based on all three approaches.*/

/* Using outer join */



/* Using set operator MINUS */



/* Using subquery */



/* 14. List the unit code,  semester, number of enrolments 
and the average mark for each unit offering in 2019. 
Include offerings without any enrolment in the list. 
Round the average to 2 digits after the decimal point. 
If the average result is 'null', display the average as 0.00. 
All values must be shown with two decimal digits. 
Order the list in increasing order of average mark, 
and when the average mark is the same, 
sort by increasing order of semester then by the unit code. */



/* 15. List all units offered in semester 2 2019 which do not have any enrolment. 
Include the unit code, unit name, and the chief examiner's name in the list. 
Order the list based on the unit code. */



/* 16. List the id and full name of students who are enrolled in both 'Introduction to databases' 
and 'Introduction to computer architecture and networks' (note: both unit names are unique)
in semester 1 2020. Order the list by the student id.*/



/* 17. Given that the payment rate for a tutorial is $42.85 per hour 
and the payment rate for a lecture is $75.60 per hour, 
calculate the weekly payment per type of class for each staff member in semester 1 2020. 
In the display, include staff id, staff name, type of class (lecture or tutorial), 
number of classes, number of hours (total duration), 
and weekly payment (number of hours * payment rate). 
Order the list by increasing order of staff id and for a given staff id by type of class.*/


    
/* 18. Given that the payment rate for a tutorial is $42.85 per hour 
and the payment rate for a lecture is $75.60 per hour, 
calculate the total weekly payment (the sum of both tutorial and lecture payments) 
for each staff member in semester 1 2020. 
In the display, include staff id, staff name, total weekly payment for tutorials, 
total weekly payment for lectures and the total weekly payment. 
If the payment is null, show it as $0.00. 
Order the list by increasing order of staff id.*/


    
/* 19. Assume that all units are worth 6 credit points each, 
calculate each student's Weighted Average Mark (WAM) and GPA. 
Please refer to these Monash websites: https://www.monash.edu/exams/results/wam 
and https://www.monash.edu/exams/results/gpa for more information about WAM and GPA respectively. 
Do not include NULL, WH or DEF grade in the calculation.

Include student id, student full name (in a 40 characters wide column headed student_fullname), 
WAM and GPA in the display. Order the list by descending order of WAM then descending order of GPA.
If two students have the same WAM and GPA, order them by their respective id.
*/




