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

