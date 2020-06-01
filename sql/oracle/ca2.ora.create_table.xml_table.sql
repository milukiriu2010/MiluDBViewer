-- # http://viralpatel.net/blogs/oracle-xmltable-tutorial/


CREATE TABLE EMPLOYEES
(
   id     NUMBER,
   data   XMLTYPE
);

INSERT INTO EMPLOYEES
     VALUES (1, xmltype ('<Employees>
    <Employee emplid="1111" type="admin">
        <firstname>John</firstname>
        <lastname>Watson</lastname>
        <age>30</age>
        <email>johnwatson@sh.com</email>
    </Employee>
    <Employee emplid="2222" type="admin">
        <firstname>Sherlock</firstname>
        <lastname>Homes</lastname>
        <age>32</age>
        <email>sherlock@sh.com</email>
    </Employee>
    <Employee emplid="3333" type="user">
        <firstname>Jim</firstname>
        <lastname>Moriarty</lastname>
        <age>52</age>
        <email>jim@sh.com</email>
    </Employee>
    <Employee emplid="4444" type="user">
        <firstname>Mycroft</firstname>
        <lastname>Holmes</lastname>
        <age>41</age>
        <email>mycroft@sh.com</email>
    </Employee>
</Employees>'));


INSERT INTO EMPLOYEES
     VALUES (2, xmltype ('<Employees>
    <Employee emplid="4444" type="admin">
        <firstname>Ariana</firstname>
        <lastname>Grande</lastname>
        <age>24</age>
        <email>johnwatson@sh.com</email>
    </Employee>
    <Employee emplid="5555" type="admin">
        <firstname>Selena</firstname>
        <lastname>Gomez</lastname>
        <age>25</age>
        <email>sherlock@sh.com</email>
    </Employee>
    <Employee emplid="6666" type="user">
        <firstname>Demi</firstname>
        <lastname>Lovato</lastname>
        <age>25</age>
        <email>jim@sh.com</email>
    </Employee>
    <Employee emplid="7777" type="user">
        <firstname>Katy</firstname>
        <lastname>Perry</lastname>
        <age>33</age>
        <email>mycroft@sh.com</email>
    </Employee>
</Employees>'));
