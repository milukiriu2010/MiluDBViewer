create table t_soccer_player_list
(
	id              int(10)       primary key  auto_increment,
	english_name    varchar(40)  not null,
	japanese_name   varchar(40),
	org_lang_name   varchar(40),
	nick_name       varchar(20),
	country_id      int(4),
	position        varchar(2),
	birthday        date,
	foreign key( country_id ) references m_country( ID )
);

create index i_t_soccer_player_list_english_name on t_soccer_player_list ( english_name );


insert into t_soccer_player_list
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Eiji Kawashima',
'川島 永嗣',
NULL,
NULL,
1,
'GK',
'1983-03-20'
);

insert into t_soccer_player_list 
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Maya Yoshida',
'吉田 麻也',
NULL,
NULL,
1,
'DF',
'1988-08-24'
);

insert into t_soccer_player_list 
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Hiroki Sakai',
'酒井 宏',
NULL,
NULL,
1,
'DF',
'1990-04-12'
);

insert into t_soccer_player_list 
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Yuto Nagatomo',
'長友 佑都',
NULL,
NULL,
1,
'DF',
'1986-09-12'
);

insert into t_soccer_player_list 
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Makoto Hasebe',
'長谷部 誠',
NULL,
NULL,
1,
'MF',
'1984-01-18'
);

insert into t_soccer_player_list 
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Genki Haraguchi',
'原口　元気',
NULL,
NULL,
1,
'MF',
'1991-05-09'
);

insert into t_soccer_player_list 
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Takashi Inui',
'乾 貴士',
NULL,
NULL,
1,
'MF',
'1988-06-02'
);

insert into t_soccer_player_list 
( english_name, japanese_name, org_lang_name, nick_name, country_id, position, birthday )
values
(
'Yuya Osako',
'大迫 勇也',
NULL,
NULL,
1,
'FW',
'1990-05-18'
);
