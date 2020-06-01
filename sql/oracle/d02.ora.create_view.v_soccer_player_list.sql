create view v_soccer_player_list
as
select
	p.english_name	english_name,
	p.japanese_name	japanese_name,
	p.nick_name	    nick_name,
	c.english       country_name,
	p.birthday		birthday
from
	t_soccer_player_list p,
	m_country		     c
where
	p.country_id = c.id
/
