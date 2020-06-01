with tmp as
(
  select
      io.ORDER_NO, io.LINE_ID, io.UPDATE_DATE, SUBSTR(io.SADDR,0,3) ADDR
    from
      ipoe_order io
    where
      SADDR is not null
      and io.TYPE = 1  -- ìoò^
union all
  select
      io.ORDER_NO, io.LINE_ID, io.UPDATE_DATE, SUBSTR(ftu.ADDR,0,3) ADDR
    from
      fc_t_uinfo ftu,
      ipoe_order io
    where
      ftu.T_LINE_ID = io.T_LINE_ID
      and ftu.LINE_ID = io.LINE_ID
      and ftu.TYPE = 3 -- ê›íuèÍèä
      and io.TYPE = 1  -- ìoò^
      and io.SADDR is null
)
select ADDR, count(*)
  from tmp a
where not exists (
    select 1
    from tmp b
    where a.LINE_ID = b.LINE_ID
    and a.UPDATE_DATE < b.UPDATE_DATE
    and a.ORDER_NO <> b.ORDER_NO
  )
  group by ADDR
  order by count(*)
;


---------------------------------------------------------------------------------------------------------


with
  temp_ipoe_data as (
    select
        idh1.DATA_NO, idh1.ORDER_NO_CLOSE, idh1.ORDER_NO_OPEN, idh1.STATUS, idh1.BACKUP_DATE
      from
        IPOE_DATA_HISTORY idh1
        left outer join IPOE_DATA_HISTORY idh2
          on idh1.DATA_NO = idh2.DATA_NO
            and idh2.BACKUP_DATE < to_date('20171001','YYYYMMDD')
            and idh1.BACKUP_DATE < idh2.BACKUP_DATE
      where
        idh2.DATA_NO is null
        and idh1.BACKUP_DATE < to_date('20171001','YYYYMMDD')
  ),
  temp_ipoe_order as (
    select
        ioh1.ORDER_NO, ioh1.DATA_NO, ioh1.STATUS, ioh1.BACKUP_DATE
      from
        IPOE_ORDER_HISTORY ioh1
        left outer join IPOE_ORDER_HISTORY ioh2
          on ioh1.ORDER_NO = ioh2.ORDER_NO
            and ioh2.BACKUP_DATE < to_date('20171001','YYYYMMDD')
            and ioh1.BACKUP_DATE < ioh2.BACKUP_DATE
      where
        ioh2.DATA_NO is null
        and ioh1.BACKUP_DATE < to_date('20171001','YYYYMMDD')
  )
select
    id.STATUS, io.STATUS, count(*)
  from
    temp_ipoe_data id,
    temp_ipoe_order io
  where
    id.DATA_NO = io.DATA_NO
    and (
      id.ORDER_NO_CLOSE = io.ORDER_NO
      or (
        id.ORDER_NO_OPEN = io.ORDER_NO
        and id.ORDER_NO_CLOSE = '999'
      )
    )
  group by id.STATUS, io.STATUS
  order by id.STATUS, io.STATUS
;


--- PLAN_TABLE_OUTPUT --------------------
Plan hash value: 4109401981
 
--------------------------------------------------------------------------------------------------------
| Id  | Operation                       | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                |                      |     1 |   124 |   242   (1)| 00:00:03 |
|   1 |  SORT GROUP BY                  |                      |     1 |   124 |   242   (1)| 00:00:03 |
|*  2 |   FILTER                        |                      |       |       |            |          |
|   3 |    NESTED LOOPS OUTER           |                      |     1 |   124 |   241   (0)| 00:00:03 |
|   4 |     NESTED LOOPS                |                      |     1 |    98 |   236   (0)| 00:00:03 |
|*  5 |      FILTER                     |                      |       |       |            |          |
|*  6 |       HASH JOIN RIGHT OUTER     |                      |    20 |  1180 |   136   (0)| 00:00:02 |
|*  7 |        TABLE ACCESS FULL        | IPOE_ORDER_HISTORY   |  1978 | 55384 |    68   (0)| 00:00:01 |
|*  8 |        TABLE ACCESS FULL        | IPOE_ORDER_HISTORY   |  1978 | 61318 |    68   (0)| 00:00:01 |
|*  9 |      TABLE ACCESS BY INDEX ROWID| IPOE_DATA_HISTORY    |     1 |    39 |     5   (0)| 00:00:01 |
|* 10 |       INDEX RANGE SCAN          | I1_IPOE_DATA_HISTORY |     2 |       |     2   (0)| 00:00:01 |
|* 11 |     TABLE ACCESS BY INDEX ROWID | IPOE_DATA_HISTORY    | 60833 |  1544K|     5   (0)| 00:00:01 |
|* 12 |      INDEX RANGE SCAN           | I1_IPOE_DATA_HISTORY |     2 |       |     2   (0)| 00:00:01 |
--------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - filter("IDH2"."DATA_NO" IS NULL)
   5 - filter("IOH2"."DATA_NO" IS NULL)
   6 - access("IOH1"."ORDER_NO"="IOH2"."ORDER_NO"(+))
       filter("IOH1"."BACKUP_DATE"<"IOH2"."BACKUP_DATE"(+))
   7 - filter("IOH2"."BACKUP_DATE"(+)<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss'))
   8 - filter("IOH1"."BACKUP_DATE"<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss'))
   9 - filter("IDH1"."BACKUP_DATE"<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss') 
              AND ("IDH1"."ORDER_NO_CLOSE"="IOH1"."ORDER_NO" OR "IDH1"."ORDER_NO_OPEN"="IOH1"."ORDER_NO" AND 
              "IDH1"."ORDER_NO_CLOSE"='999'))
  10 - access("IDH1"."DATA_NO"="IOH1"."DATA_NO")
  11 - filter("IDH2"."BACKUP_DATE"(+)<TO_DATE(' 2017-10-01 00:00:00', 'syyyy-mm-dd hh24:mi:ss') 
              AND "IDH1"."BACKUP_DATE"<"IDH2"."BACKUP_DATE"(+))
  12 - access("IDH1"."DATA_NO"="IDH2"."DATA_NO"(+))
------------------------------------------
