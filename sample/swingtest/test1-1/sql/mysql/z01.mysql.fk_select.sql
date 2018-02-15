-- https://stackoverflow.com/questions/201621/how-do-i-see-all-foreign-keys-to-a-table-or-column

SELECT 
  TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME, REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME
FROM
  INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE
  REFERENCED_TABLE_SCHEMA = 'sakila'
-- AND
 -- REFERENCED_TABLE_NAME = '<table>';
order by referenced_table_name, referenced_column_name
