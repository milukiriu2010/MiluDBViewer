-- http://www.dba-oracle.com/t_xmltable.htm

CREATE TABLE Employee_details
(
Name VARCHAR2(30),
Details xmltype
);

INSERT INTO employee_details VALUES ('Yegappan Alagappan',
    '<Employee>
<DOB>1980-04-01</DOB>
<Designation>Technical Architect</Designation>
<Hire_date>2015-05-18</Hire_date>
<Job_ID>1011025</Job_ID>
<Salary>55000 $</Salary></Employee>'
  );
 
INSERT INTO employee_details VALUES ('Sivaramakrishnan',
    '<Employee>
<DOB>1988-08-16</DOB>
<Designation>Technical Lead</Designation>
<Hire_date>2016-05-18</Hire_date>
<Job_ID>1011024</Job_ID>
<Salary>35000 $</Salary></Employee>'
  );
 
INSERT INTO employee_details VALUES ('Thiyagu Thanthoni',
    '<Employee>
<DOB>1981-03-24</DOB>
<Designation>Vice President</Designation>
<Hire_date>2016-07-21</Hire_date>
<Job_ID>1011023</Job_ID>
<Salary>40000 $</Salary></Employee>'
  );
 
INSERT INTO employee_details VALUES ('Karthick Natarajan',
    '<Employee>
<DOB>1988-02-14</DOB>
<Designation>Senior Manager</Designation>
<Hire_date>2016-09-23</Hire_date>
<Job_ID>1011022</Job_ID>
<Salary>30000 $</Salary></Employee>'
  );
 
INSERT INTO employee_details VALUES ('Avinash Kamal',
    '<Employee>
<DOB>1988-07-27</DOB>
<Designation>Senior Software Engineer</Designation>
<Hire_date>2016-09-29</Hire_date>
<Job_ID>1011021</Job_ID>
<Salary>20000 $</Salary></Employee>'
  );
 
INSERT INTO employee_details VALUES ('Charles Jagan',
    '<Employee>
<DOB>1988-05-01</DOB>
<Designation>Analyst-I Application Programmer</Designation>
<Hire_date>2016-07-24</Hire_date>
<Job_ID>101101</Job_ID>
<Salary>10000 $</Salary></Employee>'
  );


SELECT employee_details.name,
  emp.*
FROM employee_details, xmltable('/Employee' passing employee_details.details columns
DOB DATE path '/Employee/DOB',
Designation VARCHAR2(100) path '/Employee/Designation',
Hire_date DATE path '/Employee/Hire_date',
Job_ID NUMBER path '/Employee/Job_ID',
Salary VARCHAR2(10) path '/Employee/Salary') Emp
ORDER BY employee_details.name ASC;
