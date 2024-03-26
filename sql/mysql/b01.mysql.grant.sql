-- # 
-- # 
-- # To see function & procedure lists, "execute privileges" & "select mysql.proc" are required.
-- # 
-- # 

create user 'milu'@'%' identified by 'milu';

grant select on *.* to 'milu'@'%';
grant insert on *.* to 'milu'@'%';
grant update on *.* to 'milu'@'%';
grant delete on *.* to 'milu'@'%';
grant execute on *.* to 'milu'@'%';
grant create on *.* to 'milu'@'%';
grant create routine on *.* to 'milu'@'%';
grant create view on *.* to 'milu'@'%';
grant show view on *.* to 'milu'@'%';
grant trigger on *.* to 'milu'@'%';
grant drop on *.* to 'milu'@'%';
grant alter on *.* to 'milu'@'%';
grant references on *.* to 'milu'@'%';
grant index on miludb.* to 'milu'@'%';

grant execute on *.* to 'milu'@'%';
-- # grant execute on *.* to 'milu'@'%';

flush privileges;

-- # ---------------------------------------------------

create user 'user01'@'%' identified by 'user01';

grant select on sakila.* to 'user01'@'%';


