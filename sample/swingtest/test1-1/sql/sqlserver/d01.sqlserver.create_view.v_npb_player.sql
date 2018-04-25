create view v_npb_player
as
select
	p.id		player_id,
	p.team_id	team_id,
	concat( t.head_waname, t.tail_waname )	team_name,
	p.sei		sei,
	p.mei		mei,
	p.pos_id	pos_id,
	pos.name	pos_name
from
	t_npb_player_list p,
	m_npb_team_list   t,
	m_npb_position	  pos
where
	p.team_id = t.id
	and
	p.pos_id = pos.id
;
