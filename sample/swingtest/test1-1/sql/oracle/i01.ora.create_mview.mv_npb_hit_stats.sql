create materialized view mv_npb_hit_stats
as
select
	p.sei||p.mei	p_name,
	t.head_waname	t_name,
	p.pos_id	pos_id,
	h.tyear		tyear,
	h.game		game,
	h.pa		pa,
	h.ab		ab,
	h.run		run,
	h.hit		hit,
	h.b1		b1,
	h.b2		b2,
	h.b3		b3,
	h.hr		hr,
	h.rbi		rbi,
	h.sb		sb,
	h.cs		cs,
	h.bb		bb,
	h.hbp		hbp,
	h.so		so,
	h.sh		sh,
	h.sf		sf,
	h.ibb		ibb
from
	t_npb_player_list p,
	m_npb_team_list   t,
	m_npb_position	  pos,
	t_npb_hit_stats	  h
where
	p.team_id = t.id
	and
	p.pos_id = pos.id
	and
	p.id = h.id
order by p.id
/
