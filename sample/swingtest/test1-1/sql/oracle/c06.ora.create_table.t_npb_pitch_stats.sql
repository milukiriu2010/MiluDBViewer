create table t_npb_pitch_stats
(
	id		number(10),	-- # プレイヤー番号(Player No.)
	tyear		number(4),	-- # 年度(The year)
	game		number(4),	-- # 試合数(Games)
	cg		number(4),	-- # 完投(Complete Games)
	sho		number(4),	-- # 完封(Shoutouts)
	wins		number(4),	-- # 勝利(Wins)
	losses		number(4),	-- # 敗戦(Losses)
	saves		number(4),	-- # セーブ(Saves)
	ip		number(6,2),	-- # 投球回数(Innings Pitched)
	hit		number(4),	-- # 被安打(Hits)
	hr		number(4),	-- # 被本塁打(Home Runs)
	so		number(4),	-- # 奪三振(Strikeouts)
	bb		number(4),	-- # 与四球(Base on Balls)
	hbp		number(4),	-- # 与死球(Hit By Pitch)
	wp		number(4),	-- # 暴投(Wild Pitch)
	bk		number(4),	-- # ボーク(Balks)
	run		number(4),	-- # 失点(Runs)
	er		number(4),	-- # 自責点(Earned Runs)
	constraint pk_t_npb_pitch_stats primary key( id, tyear ),
	constraint fk_t_npb_pitch_stats_id foreign key( id ) references t_npb_player_list( id )
)
/

comment on table t_npb_pitch_stats is '年度別投手成績';
comment on column t_npb_pitch_stats.cg is '完投(Complete Games)';
comment on column t_npb_pitch_stats.sho is '完封(Shoutouts)';
comment on column t_npb_pitch_stats.wins is '勝利(Wins)';
comment on column t_npb_pitch_stats.losses is '敗戦(Losses)';
comment on column t_npb_pitch_stats.saves is 'セーブ(Saves)';
comment on column t_npb_pitch_stats.ip is '投球回数(Innings Pitched)';
comment on column t_npb_pitch_stats.hit is '被安打(Hits)';
comment on column t_npb_pitch_stats.hr is '被本塁打(Home Runs)';
comment on column t_npb_pitch_stats.so is '奪三振(Strikeouts)';
comment on column t_npb_pitch_stats.bb is '与四球(Base on Balls)';
comment on column t_npb_pitch_stats.hbp is '与死球(Hit By Pitch)';
comment on column t_npb_pitch_stats.bk is 'ボーク(Balks)';
comment on column t_npb_pitch_stats.run is '失点(Runs)';
comment on column t_npb_pitch_stats.er is '自責点(Earned Runs)';
