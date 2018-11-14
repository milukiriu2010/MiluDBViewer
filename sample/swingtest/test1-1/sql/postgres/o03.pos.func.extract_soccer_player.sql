create or replace function extract_soccer_player
(
	i_country_id  in   integer,
	i_position    in   varchar,
	o_name        out  varchar,
	o_birthday    out  date
)
AS
$$
	select english_name o_name, birthday o_birthday
	from t_soccer_player_list where country_id = i_country_id and position = i_position;
$$ LANGUAGE sql;
