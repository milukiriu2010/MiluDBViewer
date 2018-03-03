create table t_npb_pitch_stats
(
	id		number(10),	-- # プレイヤー番号(Player No.)
	tyear	number(4),	-- # 年度(The year)
	game	number(4),	-- # 試合数(Games)
	cg		number(4),	-- # 完投(Complete Games)
	sho		number(4),	-- # 完封(Shoutouts)
	wins	number(4),	-- # 勝利(Wins)
	losses	number(4),	-- # 敗戦(Losses)
	saves	number(4),	-- # セーブ(Saves)
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
comment on column t_npb_pitch_stats.bk is 'ボ－ク(Balks)';
comment on column t_npb_pitch_stats.run is '失点(Runs)';
comment on column t_npb_pitch_stats.er is '自責点(Earned Runs)';


insert into t_npb_pitch_stats
( id, tyear, game, cg, sho, wins, losses, saves, ip, hit, hr, so, bb, hbp, wp, bk, run, er )
values
( 1000, 2010, 12, 0, 0, 8, 0, 0, 62.1, 63, 3, 57, 13, 5, 2, 0, 23, 18 );

insert into t_npb_pitch_stats
( id, tyear, game, cg, sho, wins, losses, saves, ip, hit, hr, so, bb, hbp, wp, bk, run, er )
values
( 2001, 2010, 27, 1, 1, 13, 8, 0, 157, 152, 11, 140, 55, 9, 4, 1, 64, 57 );

insert into t_npb_pitch_stats
( id, tyear, game, cg, sho, wins, losses, saves, ip, hit, hr, so, bb, hbp, wp, bk, run, er )
values
( 3001, 2010, 25, 1, 1, 12, 9, 0, 156.2, 159, 19, 115, 25, 5, 5, 0, 67, 61 );

insert into t_npb_pitch_stats
( id, tyear, game, cg, sho, wins, losses, saves, ip, hit, hr, so, bb, hbp, wp, bk, run, er )
values
( 4001, 2010, 21, 4, 4, 12, 7, 0, 147.2, 147, 17, 112, 24, 4, 6, 1, 55, 48 );

insert into t_npb_pitch_stats
( id, tyear, game, cg, sho, wins, losses, saves, ip, hit, hr, so, bb, hbp, wp, bk, run, er )
values
( 6001, 2010, 28, 6, 2, 15, 8, 0, 251.2, 166, 15, 174, 46, 7, 2, 0, 55, 53 );
