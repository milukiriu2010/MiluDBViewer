create table m_country
(
	ID          int(5)  primary key,
	English     varchar(40),
	Japanese    varchar(40),
	Chinese     varchar(40),
	French      varchar(40),
	Spanish     varchar(40)
);

insert into m_country 
( ID, English, Japanese, Chinese, French, Spanish )
values( 1, 'America', 'アメリカ', '美国', 'Amérique', 'America' );

insert into m_country 
( ID, English, Japanese, Chinese, French, Spanish )
values( 81, 'Japan', '日本', '日本', 'Japon', 'Japon' );
