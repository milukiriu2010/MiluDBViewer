DELIMITER //
create procedure extract_soccer_player
(
	in  i_country_id   int(3),
	in  i_position     varchar(2),
	out o_name         varchar(20),
	out o_birthday     date
)
begin
	select english_name as o_name, birthday as o_birthday
	from t_soccer_player_list where country_id = i_country_id and position = i_position;
end
//
DELIMITER ;
