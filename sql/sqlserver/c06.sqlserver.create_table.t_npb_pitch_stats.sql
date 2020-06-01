create table t_npb_pitch_stats
(
    id            int  not null,    -- # プレイヤー番号(Player No.)
    tyear         int  not null,    -- # 年度(The year)
    game          int,    -- # 試合数(Games)
    cg            int,    -- # 完投(Complete Games)
    sho           int,    -- # 完封(Shoutouts)
    wins          int,    -- # 勝利(Wins)
    losses        int,    -- # 敗戦(Losses)
    saves         int,    -- # セーブ(Saves)
    ip            numeric(6,2),    -- # 投球回数(Innings Pitched)
    hit           int,    -- # 被安打(Hits)
    hr            int,    -- # 被本塁打(Home Runs)
    so            int,    -- # 奪三振(Strikeouts)
    bb            int,    -- # 与四球(Base on Balls)
    hbp           int,    -- # 与死球(Hit By Pitch)
    wp            int,    -- # 暴投(Wild Pitch)
    bk            int,    -- # ボーク(Balks)
    run           int,    -- # 失点(Runs)
    er            int     -- # 自責点(Earned Runs)
);

alter table t_npb_pitch_stats add primary key( id, tyear );
alter table t_npb_pitch_stats add foreign key( id ) references t_npb_player_list( id ) on update no action on delete no action;

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
