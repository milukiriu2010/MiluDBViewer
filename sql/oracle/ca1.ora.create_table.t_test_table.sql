create table t_test_table_a
(
	a01	char(3),
	a02	varchar2(13),
	a03	nchar(5),
	a04	nvarchar2(15),
	a05	clob,
	a06	nclob,
	a07	long,
	a08	number,
	a09	number(5),
	a10	number(10,3),
	a11	date,
	a12	timestamp,
	a13	raw(10),
	a14	timestamp with time zone,
	a15	timestamp with local time zone,
	a16	interval year to month,
	a17	interval day to second
)
/

-- a16 1年と123ヶ月
-- a17 1日と1秒
insert into t_test_table_a ( a01,a02,a03,a04,a05,a06,a07,a08,a09,a10,a11,a12,a13,a14,a15,a16,a17 )values('1', '123', '2', '234', to_clob( 'clob data' ), to_clob( 'nclob data' ), to_clob( 'long data' ), 3, 333, 3.123, sysdate, sysdate, utl_raw.cast_to_raw( 'raw data' ), sysdate, sysdate, INTERVAL '135' MONTH(3), INTERVAL '1 0:0:1' DAY TO SECOND );



create table t_test_table_b
(
	b01	char(3),
	b02	long raw
)
/

insert into t_test_table_b ( b01,b02 )values('1', utl_raw.cast_to_raw( 'long raw data' ) );
