 select
    fk.owner,
    fk.table_name, 
    fk.constraint_name,   
    fk.r_constraint_name,   
    fk.position, 
    fk.column_name,   
    cref.table_name r_table_name,   
    ccref.position 
    r_position, 
    ccref.column_name 
    r_column_name 
 from
    all_constraints cref, 
    all_cons_columns ccref, 
    (   
      select 
        c.owner,
        c.table_name, 
        c.constraint_name,     
        c.r_constraint_name,     
        cc.position, 
        cc.column_name   
      from 
        all_constraints c, 
        all_cons_columns cc   
      where 
        c.table_name  = cc.table_name   
        and 
        c.constraint_name = cc.constraint_name   
        and 
        c.constraint_type = 'R'   
      order by cc.table_name, cc.constraint_name, cc.position 
    ) fk 
  where 
    fk.r_constraint_name = cref.constraint_name 
    and 
    cref.constraint_name = ccref.constraint_name 
    and 
    fk.position = ccref.position 
  order by fk.owner, fk.table_name, fk.constraint_name, fk.position
