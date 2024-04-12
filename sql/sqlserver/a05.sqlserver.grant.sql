use miludb;
grant control on schema::miluschema to milu;
go
grant alter on schema::milushema to milu;
go
grant create table to milu;
go
exec sp_addrolemember 'db_owner', 'milu'
exec sp_addrolemember 'db_datareader', 'milu'
go

