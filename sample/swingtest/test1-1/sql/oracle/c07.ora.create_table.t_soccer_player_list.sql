create table t_soccer_player_list
(
	id				number(10),
	english_name	varchar2(40)  not null,
	japanese_name	varchar2(40),
	chinese_name	varchar2(40),
	french_name		varchar2(40),
	spanish_name	varchar2(40),
	nick_name		varchar2(20),
	country_id		number(4),
	position		varchar2(2),
	birthday		date,
	constraint pk_t_soccer_player_list_id primary key ( id ),
	constraint fk_t_soccer_player_list_country_id foreign key( country_id ) references m_country( ID )
)
/

create index i_t_soccer_player_list_engname on t_soccer_player_list( english_name );

create sequence seq_t_soccer_player_list_id minvalue 1000000 maxvalue 9999999 increment by 10 start with 1000000 cache 20 noorder;

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Eiji Kawashima',
'川島 永嗣',
NULL,
NULL,
NULL,
NULL,
1,
'GK',
to_date( '19830320', 'YYYYMMDD' )
);

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Maya Yoshida',
'吉田 麻也',
NULL,
NULL,
NULL,
NULL,
1,
'DF',
to_date( '19880824', 'YYYYMMDD' )
);

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Hiroki Sakai',
'酒井 宏',
NULL,
NULL,
NULL,
NULL,
1,
'DF',
to_date( '19900412', 'YYYYMMDD' )
);

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Yuto Nagatomo',
'長友 佑都',
NULL,
NULL,
NULL,
NULL,
1,
'DF',
to_date( '19860912', 'YYYYMMDD' )
);

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Makoto Hasebe',
'長谷部 誠',
NULL,
NULL,
NULL,
NULL,
1,
'MF',
to_date( '19840118', 'YYYYMMDD' )
);

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Genki Haraguchi',
'原口　元気',
NULL,
NULL,
NULL,
NULL,
1,
'MF',
to_date( '19910509', 'YYYYMMDD' )
);

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Takashi Inui',
'乾 貴士',
NULL,
NULL,
NULL,
NULL,
1,
'MF',
to_date( '19880602', 'YYYYMMDD' )
);

insert into t_soccer_player_list values
(
seq_t_soccer_player_list_id.nextval,
'Yuya Osako',
'大迫 勇也',
NULL,
NULL,
NULL,
NULL,
1,
'FW',
to_date( '19900518', 'YYYYMMDD' )
);
