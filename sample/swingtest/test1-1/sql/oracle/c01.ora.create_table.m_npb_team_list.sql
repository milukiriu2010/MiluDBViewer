create table m_npb_team_list
(
	id		varchar2(10),
	head_name	varchar2(40) not null,
	tail_name	varchar2(40) not null,
	head_waname	varchar2(40) not null,
	tail_waname	varchar2(40) not null,
	another_waname	varchar2(40),
	league		number(1) not null, -- # 1:central/2:pacific
	start_date	date default to_date( '19700101', 'YYYYMMDD' ) not null,
	end_date	date default to_date( '99991231', 'YYYYMMDD' ) not null,
	constraint pk_m_npb_team_list primary key( id )
)
/

comment on table m_npb_team_list is 'プロ野球チームリスト';
comment on column m_npb_team_list.head_name is 'チームオーナー英名';
comment on column m_npb_team_list.tail_name is 'チームキャラクター英名';

create bitmap index bp_m_npb_team_list_league on m_npb_team_list( league );
--create unique index ui_m_npb_team_list_name on m_npb_team_list( head_name, tail_name );
alter table m_npb_team_list add constraint ui_m_npb_team_list_name unique( head_name, tail_name );
create unique index ui_m_npb_team_list_waname on m_npb_team_list( head_waname, tail_waname );
--alter table m_npb_team_list add constraint ui_m_npb_team_list_waname unique( head_waname, tail_waname );
--alter table m_npb_team_list disable constraint ui_m_npb_team_list_waname;
alter index ui_m_npb_team_list_waname invisible;
create index f_m_npb_team_list_name_hnu on m_npb_team_list( upper( head_name ) );
create index f_m_npb_team_list_name_tnu on m_npb_team_list( upper( tail_name ) );
-- Function Indexを不可にできない？
--alter table m_npb_team_list disable constraint f_m_npb_team_list_name_tnu;
--alter index f_m_npb_team_list_name_tnu disable;

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'HT', 'Hanshin', 'Tigers', '阪神', 'タイガース', NULL, 1 );

insert into m_npb_team_list
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'YG', 'Yomiuri', 'Giants', '読売', 'ジャイアンツ', '巨人', 1 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'CD', 'Chunichi', 'Dragons', '中日', 'ドラゴンズ', NULL, 1 );

insert into m_npb_team_list
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'YS', 'Yakult', 'Swallows', 'ヤクルト', 'スワローズ', NULL, 1 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'YB', 'Yokohama', 'Baystars', '横浜', 'ベイスターズ', NULL, 1 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'HC', 'Hiroshima', 'Carp', '広島', 'カープ', NULL, 1 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'DH', 'Softbank', 'Hawks', 'ソフトバンク', 'ホークス', NULL, 2 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'SL', 'Seibu', 'Lions', '西武', 'ライオンズ', NULL, 2 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'LM', 'Lotte', 'Marines', 'ロッテ', 'マリーンズ', NULL, 2 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'NF', 'NipponHam', 'Fighters', '日本ハム', 'ファイターズ', NULL, 2 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'RE', 'Rakuten', 'Eagles', '楽天', 'イーグルス', NULL, 2 );

insert into m_npb_team_list 
( id, head_name, tail_name, head_waname, tail_waname, another_waname, league )
values
( 'OB', 'Orix', 'Buffaloes', 'オリックス', 'バッファローズ', NULL, 2 );
