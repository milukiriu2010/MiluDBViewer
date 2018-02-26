create table t_npb_pitch_stats
(
    id            int(10),   -- # プレイヤー番号(Player No.)
    tyear         int(4),    -- # 年度(The year)
    game          int(4),    -- # 試合数(Games)
    cg            int(4),    -- # 完投(Complete Games)
    sho           int(4),    -- # 完封(Shoutouts)
    wins          int(4),    -- # 勝利(Wins)
    losses        int(4),    -- # 敗戦(Losses)
    saves         int(4),    -- # セーブ(Saves)
    ip            numeric(6,2),    -- # 投球回数(Innings Pitched)
    hit           int(4),    -- # 被安打(Hits)
    hr            int(4),    -- # 被本塁打(Home Runs)
    so            int(4),    -- # 奪三振(Strikeouts)
    bb            int(4),    -- # 与四球(Base on Balls)
    hbp           int(4),    -- # 与死球(Hit By Pitch)
    wp            int(4),    -- # 暴投(Wild Pitch)
    bk            int(4),    -- # ボーク(Balks)
    run           int(4),    -- # 失点(Runs)
    er            int(4),    -- # 自責点(Earned Runs)
    primary key( id, tyear ),
    foreign key( id ) references t_npb_player_list( id )
);

