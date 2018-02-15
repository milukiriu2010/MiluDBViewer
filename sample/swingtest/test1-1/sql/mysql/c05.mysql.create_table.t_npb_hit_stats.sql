create table t_npb_hit_stats
(
    id      int(10),   -- # プレイヤー番号(Player No.)
    tyear   int(4),    -- # 年度(The year)
    game    int(4),    -- # 試合数(Games)
    pa      int(4),    -- # 打席(Plate Appearances)
    ab      int(4),    -- # 打数(At Bat)
    run     int(4),    -- # 得点(Runs)
    hit     int(4),    -- # 安打(Hits)
    b1      int(4),    -- # 単打(Singles)
    b2      int(4),    -- # 2塁打(Doubles)
    b3      int(4),    -- # 3塁打(Triples)
    hr      int(4),    -- # ホームラン(Home Runs)
    rbi     int(4),    -- # 打点(Runs Batted In)
    sb      int(4),    -- # 盗塁(Stolen Bases)
    cs      int(4),    -- # 盗塁死(Caught Stealing)
    bb      int(4),    -- # 四球(Base on Balls)
    hbp     int(4),    -- # 死球(Hit By Pitch)
    so      int(4),    -- # 三振(Strikeouts)
    sh      int(4),    -- # 犠打(Sacrifice Hits)
    sf      int(4),    -- # 犠飛(Sacrifice Flies)
    ibb     int(4),    -- # 敬遠四球(Intentional Base On Balls)
    primary key( id, tyear ),
    foreign key( id ) references t_npb_player_list( id )
);



