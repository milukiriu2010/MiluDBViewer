select 
   acs1.constraint_schema   acs1_constraint_schema,
   acs1.constraint_name      acs1_constraint_name,
   acs1.table_schema           acs1_table_schema,
  acs1.table_name              acs1_table_name,
  acs2.unique_constraint_schema  acs2_constraint_schema,
  acs2.unique_constraint_name     acs2_constraint_name,
  acd.table_schema             acd_table_schema,
  acd.table_name                acd_table_name
from 
  information_schema.table_constraints  acs1
  left join 
  information_schema.referential_constraints acs2
    on
    acs1.constraint_catalog = acs2.constraint_catalog
    and
    acs1.constraint_schema = acs2.constraint_schema
    and
    acs1.constraint_name = acs2.constraint_name
  left join
  information_schema.table_constraints  acd
  on
    acs2.unique_constraint_catalog = acd.constraint_catalog
    and
    acs2.unique_constraint_schema   = acd.constraint_schema
    and
    acs2.unique_constraint_name = acd.constraint_name
where
  acs1.constraint_type = 'FOREIGN KEY'
order by acs1.constraint_name
