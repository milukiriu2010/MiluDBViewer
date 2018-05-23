create table t_soccer_player_list
(
	id              varchar(10)   primary key,
	english_name    varchar(40)   not null,
	japanese_name   varchar(40),
	org_lang_name   varchar(40),
	nick_name       varchar(20),
	country_id      int(5),
	position        varchar(2),
	birthday        date,
	foreign key( country_id ) references m_country( ID )
);

create index i_t_soccer_player_list_english_name on t_soccer_player_list ( english_name );


insert into t_soccer_player_list
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0001',
'Eiji Kawashima',
'川島 永嗣',
'川島 永嗣',
NULL,
81,
'GK',
'1983-03-20'
);

insert into t_soccer_player_list 
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0002',
'Maya Yoshida',
'吉田 麻也',
'吉田 麻也',
NULL,
81,
'DF',
'1988-08-24'
);

insert into t_soccer_player_list 
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0003',
'Hiroki Sakai',
'酒井 宏',
'酒井 宏',
NULL,
81,
'DF',
'1990-04-12'
);

insert into t_soccer_player_list 
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0004',
'Yuto Nagatomo',
'長友 佑都',
'長友 佑都',
NULL,
81,
'DF',
'1986-09-12'
);

insert into t_soccer_player_list 
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0005',
'Makoto Hasebe',
'長谷部 誠',
'長谷部 誠',
NULL,
81,
'MF',
'1984-01-18'
);

insert into t_soccer_player_list 
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0006',
'Genki Haraguchi',
'原口　元気',
'原口　元気',
NULL,
81,
'MF',
'1991-05-09'
);

insert into t_soccer_player_list 
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0007',
'Takashi Inui',
'乾 貴士',
'乾 貴士',
NULL,
81,
'MF',
'1988-06-02'
);

insert into t_soccer_player_list 
( id, english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'JP0008',
'Yuya Osako',
'大迫 勇也',
'大迫 勇也',
NULL,
81,
'FW',
'1990-05-18'
);
