select
  n.nspname            schema_name,
  i.indrelid::REGCLASS table_name,
  c.relname            index_name,
  i.indisunique        is_unique,
  i.indisprimary       is_primary,
  (i.indexprs IS NOT NULL) OR 
    (i.indkey::int[] @> array[0]) AS is_functional,
  case i.indisvalid 
	when 't' then 'VALID'
	else          'INVALID'
  end  status
from 
  pg_index  i  join
  pg_class  c on c.oid = i.indexrelid  join
  pg_namespace n on n.oid = c.relnamespace
where
  n.nspname = 'public'
  and
  i.indrelid = 'm_npb_team_list'::REGCLASS
order by n.nspname, table_name, index_name

--------------------------------------------------

select
  array
  (
  select
    pg_get_indexdef(i.indexrelid, 1, TRUE )
  from
    generate_subscripts(i.indkey,1) as k
  order by k
  )
from
  pg_index  i  join
  pg_class  c on c.oid = i.indexrelid  join
  pg_namespace n on n.oid = c.relnamespace
where
  n.nspname = 'public'
  and
  i.indrelid = 'countrylanguage'::REGCLASS
  and
  c.relname = 'countrylanguage_pkey'

    