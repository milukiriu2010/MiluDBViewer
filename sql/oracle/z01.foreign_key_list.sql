select  
  acs.owner            acs_owner, 
  acs.constraint_name  acs_constraint_name,  
  acs.table_name       acs_table_name,  
  acd.owner            acd_owner,  
  acd.constraint_name  acd_constraint_name,  
  acd.table_name       acd_table_name,  
  acs.status           acs_status  
from  
  (   
  select 
    owner, 
    constraint_name, 
    table_name, 
    r_owner, 
    r_constraint_name, 
    status 
  from 
    all_constraints 
  where 
    constraint_type='R' 
    and 
    owner = 'MILU' 
  ) acs,  
  all_constraints acd 
where 
  acs.r_owner = acd.owner 
  and 
  acs.r_constraint_name = acd.constraint_name  
order by acs.constraint_name

=============================================================

select 
  column_name, 
  position 
from 
  all_cons_columns 
where 
  owner = 'MILU' 
  and 
  constraint_name = 'FK_SOCCERPLAYERLST_COUNTRY_ID' 
  and 
  table_name = 'T_SOCCER_PLAYER_LIST' 
order by position

