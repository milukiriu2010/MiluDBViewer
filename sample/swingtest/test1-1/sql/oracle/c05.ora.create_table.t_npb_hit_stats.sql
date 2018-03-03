create table t_npb_hit_stats
(
	id		number(10),	-- # プレイヤー番号(Player No.)
	tyear		number(4),	-- # 年度(The year)
	game		number(4),	-- # 試合数(Games)
	pa		number(4),	-- # 打席(Plate Appearances)
	ab		number(4),	-- # 打数(At Bat)
	run		number(4),	-- # 得点(Runs)
	hit		number(4),	-- # 安打(Hits)
	b1		number(4),	-- # 単打(Singles)
	b2		number(4),	-- # 2塁打(Doubles)
	b3		number(4),	-- # 3塁打(Triples)
	hr		number(4),	-- # ホームラン(Home Runs)
	rbi		number(4),	-- # 打点(Runs Batted In)
	sb		number(4),	-- # 盗塁(Stolen Bases)
	cs		number(4),	-- # 盗塁死(Caught Stealing)
	bb		number(4),	-- # 四球(Base on Balls)
	hbp		number(4),	-- # 死球(Hit By Pitch)
	so		number(4),	-- # 三振(Strikeouts)
	sh		number(4),	-- # 犠打(Sacrifice Hits)
	sf		number(4),	-- # 犠飛(Sacrifice Flies)
	ibb		number(4),	-- # 敬遠四球(Intentional Base On Balls)
	constraint pk_t_npb_hit_stats primary key( id, tyear ),
	constraint fk_t_npb_hit_stats_id foreign key( id ) references t_npb_player_list( id )
)
/

comment on table t_npb_hit_stats is '年度別打者成績';
comment on column t_npb_hit_stats.pa is '打席(Plate Appearances)';
comment on column t_npb_hit_stats.ab is '打数(At Bat)';
comment on column t_npb_hit_stats.run is '得点(Runs)';
comment on column t_npb_hit_stats.hit is '安打(Hits)';
comment on column t_npb_hit_stats.b1 is '単打(Singles)';
comment on column t_npb_hit_stats.b2 is '2塁打(Doubles)';
comment on column t_npb_hit_stats.b3 is '3塁打(Triples)';
comment on column t_npb_hit_stats.hr is 'ホームラン(Home Runs)';
comment on column t_npb_hit_stats.rbi is '打点(Runs Batted In)';
comment on column t_npb_hit_stats.sb is '盗塁(Stolen Bases)';
comment on column t_npb_hit_stats.cs is '盗塁死(Caught Stealing)';
comment on column t_npb_hit_stats.bb is '四球(Base on Balls)';
comment on column t_npb_hit_stats.hbp is '死球(Hit By Pitch)';
comment on column t_npb_hit_stats.so is '三振(Strikeouts)';
comment on column t_npb_hit_stats.sh is '犠打(Sacrifice Hits)';
comment on column t_npb_hit_stats.sf is '犠飛(Sacrifice Flies)';
comment on column t_npb_hit_stats.ibb is '敬遠四球(Intentional Base On Balls)';


insert into t_npb_hit_stats
( id, tyear, game, pa, ab, run, hit, b1, b2, b3, hr, rbi, sb, cs, bb, hbp, so, sh, sf, ibb )
values
( 1005, 2010, 144, NULL, 668, 613, 306, 214, 35, 3, 17, 91, 11, NULL, 47, 3, 70, 0, 5, NULL );

insert into t_npb_hit_stats
( id, tyear, game, pa, ab, run, hit, b1, b2, b3, hr, rbi, sb, cs, bb, hbp, so, sh, sf, ibb )
values
( 2002, 2010, 137, NULL, 591, 510, 285, 157, 24, 1, 34, 90, 1, NULL, 73, 3, 101, 0, 5, NULL );

insert into t_npb_hit_stats
( id, tyear, game, pa, ab, run, hit, b1, b2, b3, hr, rbi, sb, cs, bb, hbp, so, sh, sf, ibb )
values
( 3002, 2010, 144, NULL, 626, 547, 294, 179, 45, 2, 22, 84, 2, NULL, 64, 6, 77, 2, 7, NULL );

insert into t_npb_hit_stats
( id, tyear, game, pa, ab, run, hit, b1, b2, b3, hr, rbi, sb, cs, bb, hbp, so, sh, sf, ibb )
values
( 5001, 2010, 144, NULL, 637, 577, 253, 182, 36, 4, 9, 66, 1, NULL, 47, 6, 51, 3, 4, NULL );

