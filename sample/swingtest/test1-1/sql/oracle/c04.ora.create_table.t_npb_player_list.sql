create table t_npb_player_list
(
	id		number(10),
	team_id		varchar2(10),
	sei		varchar2(20) not null,
	mei		varchar2(20) not null,
	pos_id		varchar2(10),
	constraint pk_t_npb_player_list primary key( id ),
	constraint fk_t_npb_player_list_team_id foreign key( team_id ) references m_npb_team_list( id ),
	constraint fk_t_npb_player_list_pos_id foreign key( pos_id ) references m_npb_position( id )
)
/

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

