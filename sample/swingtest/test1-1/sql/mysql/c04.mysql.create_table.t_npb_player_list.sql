create table t_npb_player_list
(
    id          int          primary key,
    team_id     varchar(10),
    sei         varchar(20)  not null,
    mei         varchar(20)  not null,
    pos_id      varchar(10),
    foreign key( team_id ) references m_npb_team_list( id ),
    foreign key( pos_id )  references m_npb_position( id )
);


insert into t_npb_player_list
values
( 1000, 'HT', '能見', '篤史', 'P' );

insert into t_npb_player_list
values
( 1001, 'HT', '関本', '賢太郎', 'F' );

insert into t_npb_player_list
values
( 1002, 'HT', '平野', '恵一', 'S' );

insert into t_npb_player_list
values
( 1003, 'HT', '新井', '貴浩', 'T' );

insert into t_npb_player_list
values
( 1004, 'HT', '鳥谷', '敬', 'SS' );

insert into t_npb_player_list
values
( 1005, 'HT', 'マートン', 'マット', 'CF' );

insert into t_npb_player_list
values
( 2001, 'YG', '東野', '峻', 'P' );

insert into t_npb_player_list
values
( 2002, 'YG', '小笠原', '道大', 'F' );

insert into t_npb_player_list
values
( 3001, 'CD', '吉見', '一起', 'P' );

insert into t_npb_player_list
values
( 3002, 'CD', '森野', '将彦', 'T' );

insert into t_npb_player_list
values
( 4001, 'YS', '館山', '昌平', 'P' );

insert into t_npb_player_list
values
( 5001, 'YB', '内川', '聖一', 'RF' );

insert into t_npb_player_list
values
( 6001, 'HC', '前田', '健太', 'P' );

