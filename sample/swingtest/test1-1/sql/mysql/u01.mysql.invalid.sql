-- http://frodo.looijaard.name/article/mysql-identify-invalid-views

create table t1 (f1 int);
create view v1 as select * from t1;
alter table t1 rename to t2;

