-- # 
-- # 
-- # To see function & procedure lists, "execute privileges" & "select mysql.proc" are required.
-- # 
-- # 

create user 'milu'@'localhost' identified by 'milu';

grant select on *.* to 'milu'@'localhost';
grant insert on *.* to 'milu'@'localhost';
grant update on *.* to 'milu'@'localhost';
grant delete on *.* to 'milu'@'localhost';
grant execute on *.* to 'milu'@'localhost';
grant create on *.* to 'milu'@'localhost';
grant create routine on *.* to 'milu'@'localhost';
grant create view on *.* to 'milu'@'localhost';
grant show view on *.* to 'milu'@'localhost';
grant trigger on *.* to 'milu'@'localhost';
grant drop on *.* to 'milu'@'localhost';
grant alter on *.* to 'milu'@'localhost';
grant references on *.* to 'milu'@'localhost';
grant index on miludb.* to 'milu'@'localhost';

grant execute on *.* to 'milu'@'localhost';
-- # grant execute on *.* to 'milu'@'%';

flush privileges;

-- # ---------------------------------------------------

create user 'user01'@'localhost' identified by 'user01';

grant select on sakila.* to 'user01'@'localhost';


