create materialized view mv_npb_pitch_stats
as
select
	p.sei||p.mei	p_name,
	t.head_waname	t_name,
	p.pos_id	pos_id,
	pi.tyear	tyear,
	pi.game		game,
	pi.cg		cg,
	pi.sho		sho,
	pi.wins		wins,
	pi.losses	losses,
	pi.saves	saves,
	pi.ip		ip,
	pi.hit		hit,
	pi.hr		hr,
	pi.so		so,
	pi.bb		bb,
	pi.hbp		hbp,
	pi.wp		wp,
	pi.bk		bk,
	pi.run		run,
	pi.er		er
from
	t_npb_player_list p,
	m_npb_team_list   t,
	m_npb_position	  pos,
	t_npb_pitch_stats pi
where
	p.team_id = t.id
	and
	p.pos_id = pos.id
	and
	p.id = pi.id
order by p.id
/
