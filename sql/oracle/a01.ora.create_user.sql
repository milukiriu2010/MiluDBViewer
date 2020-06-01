create tablespace miludbdata datafile 'c:\myora\miludb\miludb_data01.dbf' size 100M;

create temporary tablespace miludbtemp tempfile 'c:\myora/miludb/miludb_temp01.dbf' size 100M autoextend off;

create user milu identified by milu default tablespace miludbdata temporary tablespace miludbtemp quota unlimited on users account unlock;

grant connect to milu;
grant create table to milu;
grant create view to milu;
grant create materialized view to milu;
grant resource to milu;

grant unlimited tablespace to milu;
