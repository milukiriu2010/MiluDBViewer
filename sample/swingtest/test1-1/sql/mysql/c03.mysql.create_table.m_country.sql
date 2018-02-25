create table m_country
(
	ID          int(4)  primary key,
	English     varchar(40),
	Japanese    varchar(40),
	Chinese     varchar(40),
	French      varchar(40),
	Spanish     varchar(40)
);

insert into m_country values( 1, 'Japan', '“ú–{', '“ú–{', 'Japon', 'Japon' );
