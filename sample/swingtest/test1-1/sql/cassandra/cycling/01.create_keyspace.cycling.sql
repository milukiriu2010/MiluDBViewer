CREATE KEYSPACE cycling
  WITH REPLICATION = { 
   'class' : 'SimpleStrategy', 
   'replication_factor' : 1 
  };

-------------------------------------------------------------------------

CREATE TABLE cycling.team_average (
   team_name text, 
   cyclist_name text, 
   cyclist_time_sec int, 
   race_title text, 
   PRIMARY KEY (team_name, race_title,cyclist_name));

INSERT INTO cycling.team_average (team_name, cyclist_name, cyclist_time_sec, race_title) VALUES ('UnitedHealthCare Pro Cycling Womens Team','Katie HALL',11449,'Amgen Tour of California Women''s Race presented by SRAM - Stage 1 - Lake Tahoe > Lake Tahoe');
INSERT INTO cycling.team_average (team_name, cyclist_name, cyclist_time_sec, race_title) VALUES ('UnitedHealthCare Pro Cycling Womens Team','Linda VILLUMSEN',11485,'Amgen Tour of California Women''s Race presented by SRAM - Stage 1 - Lake Tahoe > Lake Tahoe');
INSERT INTO cycling.team_average (team_name, cyclist_name, cyclist_time_sec, race_title) VALUES ('UnitedHealthCare Pro Cycling Womens Team','Hannah BARNES',11490,'Amgen Tour of California Women''s Race presented by SRAM - Stage 1 - Lake Tahoe > Lake Tahoe');
INSERT INTO cycling.team_average (team_name, cyclist_name, cyclist_time_sec, race_title) VALUES ('Velocio-SRAM','Alena AMIALIUSIK',11451,'Amgen Tour of California Women''s Race presented by SRAM - Stage 1 - Lake Tahoe > Lake Tahoe');
INSERT INTO cycling.team_average (team_name, cyclist_name, cyclist_time_sec, race_title) VALUES ('Velocio-SRAM','Trixi WORRACK',11453,'Amgen Tour of California Women''s Race presented by SRAM - Stage 1 - Lake Tahoe > Lake Tahoe');
INSERT INTO cycling.team_average (team_name, cyclist_name, cyclist_time_sec, race_title) VALUES ('TWENTY16 presented by Sho-Air','Lauren KOMANSKI',11451,'Amgen Tour of California Women''s Race presented by SRAM - Stage 1 - Lake Tahoe > Lake Tahoe');

-------------------------------------------------------------

CREATE OR REPLACE FUNCTION cycling.avgState ( state tuple<int,bigint>, val int ) 
CALLED ON NULL INPUT 
RETURNS tuple<int,bigint> 
LANGUAGE java AS 
$$ if (val !=null) { 
      state.setInt(0, state.getInt(0)+1); 
      state.setLong(1, state.getLong(1)+val.intValue()); 
      } 
   return state; $$
; 

CREATE TABLE cycling.test_avg (
    id int PRIMARY KEY,
    state frozen<tuple<int, bigint>>,
    val int);

INSERT INTO test_avg (id,state,val) values (1,(6,9949),51);
INSERT INTO test_avg (id,state,val) values (2,(79,10000),9999);

---------------------------------------------------------

CREATE OR REPLACE FUNCTION cycling.avgFinal ( state tuple<int,bigint> ) 
CALLED ON NULL INPUT 
RETURNS double 
LANGUAGE java AS 
  $$ double r = 0; 
     if (state.getInt(0) == 0) return null; 
     r = state.getLong(1); 
     r/= state.getInt(0); 
     return Double.valueOf(r); $$ 
;

---------------------------------------------------------

CREATE AGGREGATE cycling.average(int) 
SFUNC avgState 
STYPE tuple<int,bigint> 
FINALFUNC avgFinal 
INITCOND (0,0);

SELECT cycling.average(cyclist_time_sec) FROM cycling.team_average 
WHERE team_name='UnitedHealthCare Pro Cycling Womens Team' 
 AND race_title='Amgen Tour of California Women''s Race presented by SRAM - Stage 1 - Lake Tahoe > Lake Tahoe';

--------------------------------------------------------

CREATE TYPE cycling.basic_info (
  birthday timestamp,
  nationality text,
  weight text,
  height text
);

--------------------------------------------------------

CREATE TABLE cycling.cyclist_mv (cid UUID PRIMARY KEY, name text, age int, birthday date, country text);

CREATE MATERIALIZED VIEW cycling.cyclist_by_age 
AS SELECT age, name, country 
FROM cycling.cyclist_mv 
WHERE age IS NOT NULL AND cid IS NOT NULL 
PRIMARY KEY (age, cid)
WITH caching = { 'keys' : 'ALL', 'rows_per_partition' : '100' }
   AND comment = 'Based on table cyclist' ;

CREATE MATERIALIZED VIEW cycling.cyclist_by_birthday 
AS SELECT age, birthday, name, country 
FROM cyclist_mv 
WHERE birthday IS NOT NULL AND cid IS NOT NULL 
PRIMARY KEY (birthday, cid);

CREATE MATERIALIZED VIEW cycling.cyclist_by_country 
AS SELECT age,birthday, name, country 
FROM cyclist_mv 
WHERE country IS NOT NULL AND cid IS NOT NULL 
PRIMARY KEY (country, cid);

--------------------------------------------------------

https://www.datastax.com/dev/blog/new-in-cassandra-3-0-materialized-views

CREATE TABLE scores
(
  user TEXT,
  game TEXT,
  year INT,
  month INT,
  day INT,
  score INT,
  PRIMARY KEY (user, game, year, month, day)
);

	   
INSERT INTO scores (user, game, year, month, day, score) VALUES ('pcmanus', 'Coup', 2015, 05, 01, 4000);
INSERT INTO scores (user, game, year, month, day, score) VALUES ('jbellis', 'Coup', 2015, 05, 03, 1750);
INSERT INTO scores (user, game, year, month, day, score) VALUES ('yukim', 'Coup', 2015, 05, 03, 2250);
INSERT INTO scores (user, game, year, month, day, score) VALUES ('tjake', 'Coup', 2015, 05, 03, 500);
INSERT INTO scores (user, game, year, month, day, score) VALUES ('jmckenzie', 'Coup', 2015, 06, 01, 2000);
INSERT INTO scores (user, game, year, month, day, score) VALUES ('iamaleksey', 'Coup', 2015, 06, 01, 2500);
INSERT INTO scores (user, game, year, month, day, score) VALUES ('tjake', 'Coup', 2015, 06, 02, 1000);
INSERT INTO scores (user, game, year, month, day, score) VALUES ('pcmanus', 'Coup', 2015, 06, 02, 2000);

CREATE MATERIALIZED VIEW alltimehigh AS
       SELECT user FROM scores
       WHERE game IS NOT NULL AND score IS NOT NULL AND user IS NOT NULL AND year IS NOT NULL AND month IS NOT NULL AND day IS NOT NULL
       PRIMARY KEY (game, score, user, year, month, day)
       WITH CLUSTERING ORDER BY (score desc);

CREATE MATERIALIZED VIEW dailyhigh AS
       SELECT user FROM scores
       WHERE game IS NOT NULL AND year IS NOT NULL AND month IS NOT NULL AND day IS NOT NULL AND score IS NOT NULL AND user IS NOT NULL
       PRIMARY KEY ((game, year, month, day), score, user)
       WITH CLUSTERING ORDER BY (score DESC);
 
CREATE MATERIALIZED VIEW monthlyhigh AS
       SELECT user FROM scores
       WHERE game IS NOT NULL AND year IS NOT NULL AND month IS NOT NULL AND score IS NOT NULL AND user IS NOT NULL AND day IS NOT NULL
       PRIMARY KEY ((game, year, month), score, user, day)
       WITH CLUSTERING ORDER BY (score DESC);
	   
