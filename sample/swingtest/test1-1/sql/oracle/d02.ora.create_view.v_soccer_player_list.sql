create view v_soccer_player_list
as
select
	p.eng_name	eng_name,
	p.jpn_name	jpn_name,
	p.nick_name	nick_name,
	p.nationality	nationality,
	c.日本語	jpn_nationality,
	p.birthday	birthday,
	p.regist_date	regist_date
from
	t_soccer_player_list p,
	m_国名		     c
where
	p.nationality = c.english
/
