create table m_npb_team_list
(
    id               varchar(10)     primary key,
    head_name        varchar(40)     not null,
    tail_name        varchar(40)     not null,
    head_waname      varchar(40)     not null,
    tail_waname      varchar(40)     not null,
    another_waname   varchar(40)     ,
    league           int             not null,
    start_date       datetime        not null  default '1900-01-01 00:00:00.000',
    end_date         datetime        not null  default '9999-12-31 23:59:59.000'
);


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
