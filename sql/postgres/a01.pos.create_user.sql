psql -U postgres
psql -U milu -d miludb2


# https://www.codementor.io/devops/tutorial/getting-started-postgresql-server-mac-osx

create role milu with login password 'milu';

alter role  milu createdb;

---------------------------------------------

create user user01 with login password 'user01';
