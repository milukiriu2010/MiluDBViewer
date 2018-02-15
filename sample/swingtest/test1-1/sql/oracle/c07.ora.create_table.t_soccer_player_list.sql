create table t_soccer_player_list
(
	id		number(10),
	eng_name	varchar2(40),
	jpn_name	varchar2(40),
	nick_name	varchar2(20),
	nationality	varchar2(20),
	birthday	number(8),
	regist_date	date,
	constraint pk_t_soccer_player_list_id primary key ( id ),
	constraint fk_t_soccer_player_list_nation foreign key( nationality ) references m_国名( English )
)
/

create index i_t_soccer_player_list_engname on t_soccer_player_list( eng_name );

create sequence seq_t_soccer_player_list_id minvalue 1000000 maxvalue 9999999 increment by 10 start with 1000000 cache 20 noorder;
