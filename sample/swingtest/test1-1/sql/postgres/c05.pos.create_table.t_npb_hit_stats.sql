create table t_npb_hit_stats
(
    id      numeric(10),   -- # プレイヤ－番号(Player No.)
    tyear   numeric(4),    -- # 年度(The year)
    game    numeric(4),    -- # 試合数(Games)
    pa      numeric(4),    -- # 打席(Plate Appearances)
    ab      numeric(4),    -- # 打数(At Bat)
    run     numeric(4),    -- # 得点(Runs)
    hit     numeric(4),    -- # 安打(Hits)
    b1      numeric(4),    -- # 単打(Singles)
    b2      numeric(4),    -- # 2塁打(Doubles)
    b3      numeric(4),    -- # 3塁打(Triples)
    hr      numeric(4),    -- # ホ－ムラン(Home Runs)
    rbi     numeric(4),    -- # 打点(Runs Batted In)
    sb      numeric(4),    -- # 盗塁(Stolen Bases)
    cs      numeric(4),    -- # 盗塁死(Caught Stealing)
    bb      numeric(4),    -- # 四球(Base on Balls)
    hbp     numeric(4),    -- # 死球(Hit By Pitch)
    so      numeric(4),    -- # 三振(Strikeouts)
    sh      numeric(4),    -- # 犠打(Sacrifice Hits)
    sf      numeric(4),    -- # 犠飛(Sacrifice Flies)
    ibb     numeric(4),    -- # 敬遠四球(Intentional Base On Balls)
    primary key( id, tyear ),
    foreign key( id ) references t_npb_player_list( id ) on update cascade on delete no action
);

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
