create table t_soccer_player_list_history
(
	history_date	date,
	id		number(10),
	eng_name	varchar2(40),
	jpn_name	varchar2(40),
	nick_name	varchar2(20),
	nationality	varchar2(20),
	birthday	number(8),
	regist_date	date,
	constraint fk_t_soccer_player_his_nation foreign key( nationality ) references m_国名( English )
)
/

create index i_t_soccer_player_his_id on t_soccer_player_list_history( id );
create index i_t_soccer_player_his_engname on t_soccer_player_list_history( eng_name );


create or replace trigger trg_t_soccer_player_list
after delete on t_soccer_player_list for each row
declare
begin
	insert into t_soccer_player_list_history
	(
	history_date,
	id,
	eng_name,
	jpn_name,
	nick_name,
	nationality,
	birthday,
	regist_date
	)
	values
	(
	sysdate,
	:old.id,
	:old.eng_name,
	:old.jpn_name,
	:old.nick_name,
	:old.nationality,
	:old.birthday,
	:old.regist_date
	);
end;
/
