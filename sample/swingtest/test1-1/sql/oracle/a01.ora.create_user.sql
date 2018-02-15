create tablespace mydbdata datafile '/ora/oracle11g_db/app/oradata/MYDB/mydbdata01.dbf' size 100M;

create temporary tablespace mydbtemp tempfile '/ora/oracle11g_db/app/oradata/MYDB/mydbtemp01.dbf' size 100M autoextend off;

create user milu identified by tiger default tablespace mydbdata temporary tablespace mydbtemp quota unlimited on users account unlock;

grant connect to milu;
grant create table to milu;
grant create view to milu;
grant create materialized view to milu;
grant resource to milu;
