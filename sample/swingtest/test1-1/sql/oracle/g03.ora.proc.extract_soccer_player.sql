create or replace procedure extract_soccer_player
(
	i_country_id  in   number,
	i_position    in   varchar2,
	o_name        out  varchar2,
	o_birthday    out  date
)
is
begin
	select english_name, birthday into o_name, o_birthday
	from t_soccer_player_list where country_id = i_country_id and position = i_position;
end;
/
