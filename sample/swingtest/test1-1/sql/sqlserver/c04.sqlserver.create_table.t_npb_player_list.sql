create table t_npb_player_list
(
    id                int           primary key,
    team_id           varchar(10),
    sei               varchar(20)  not null,
    mei               varchar(20)  not null,
    pos_id            varchar(10),
    country_id        int
);

alter table t_npb_player_list add foreign key( team_id ) references m_npb_team_list( id )  on update cascade on delete no action;
alter table t_npb_player_list add foreign key( pos_id )  references m_npb_position( id )   on update cascade on delete no action;
alter table t_npb_player_list alter column country_id numeric(4);
alter table t_npb_player_list add foreign key( country_id ) references m_country( ID )   on update cascade on delete no action;

insert into t_npb_player_list
values
( 1000, 'HT', '�\��', '�Ďj', 'P', 81 );

insert into t_npb_player_list
values
( 1001, 'HT', '�֖{', '�����Y', 'F', 81 );

insert into t_npb_player_list
values
( 1002, 'HT', '����', '�b��', 'S', 81 );

insert into t_npb_player_list
values
( 1003, 'HT', '�V��', '�M�_', 'T', 81 );

insert into t_npb_player_list
values
( 1004, 'HT', '���J', '�h', 'SS', 81 );

insert into t_npb_player_list
values
( 1005, 'HT', '�}�[�g��', '�}�b�g', 'CF', 1 );

insert into t_npb_player_list
values
( 2001, 'YG', '����', '�s', 'P', 81 );

insert into t_npb_player_list
values
( 2002, 'YG', '���}��', '����', 'F', 81 );

insert into t_npb_player_list
values
( 3001, 'CD', '�g��', '��N', 'P', 81 );

insert into t_npb_player_list
values
( 3002, 'CD', '�X��', '���F', 'T', 81 );

insert into t_npb_player_list
values
( 4001, 'YS', '�َR', '����', 'P', 81 );

insert into t_npb_player_list
values
( 5001, 'YB', '����', '����', 'RF', 81 );

insert into t_npb_player_list
values
( 6001, 'HC', '�O�c', '����', 'P', 81 );

