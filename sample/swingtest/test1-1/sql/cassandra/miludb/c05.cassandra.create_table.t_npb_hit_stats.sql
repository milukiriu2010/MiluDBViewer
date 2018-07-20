create table t_npb_hit_stats
(
    id      int,   -- # プレイヤー番号(Player No.)
    tyear   int,    -- # 年度(The year)
    game    int,    -- # 試合数(Games)
    pa      int,    -- # 打席(Plate Appearances)
    ab      int,    -- # 打数(At Bat)
    run     int,    -- # 得点(Runs)
    hit     int,    -- # 安打(Hits)
    b1      int,    -- # 単打(Singles)
    b2      int,    -- # 2塁打(Doubles)
    b3      int,    -- # 3塁打(Triples)
    hr      int,    -- # ホームラン(Home Runs)
    rbi     int,    -- # 打点(Runs Batted In)
    sb      int,    -- # 盗塁(Stolen Bases)
    cs      int,    -- # 盗塁死(Caught Stealing)
    bb      int,    -- # 四球(Base on Balls)
    hbp     int,    -- # 死球(Hit By Pitch)
    so      int,    -- # 三振(Strikeouts)
    sh      int,    -- # 犠打(Sacrifice Hits)
    sf      int,    -- # 犠飛(Sacrifice Flies)
    ibb     int,    -- # 敬遠四球(Intentional Base On Balls)
    primary key( id, tyear )
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
