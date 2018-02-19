package milu.db.table;

public class TableDBPostgres extends TableDBAbstract 
{

	@Override
	protected String tableLstSQL( String schemaName )
	{
		String sql = 
			"select \n" +
			"  c.relname  \n" +
			"from \n" +
			"  pg_class c join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n" +
			"where \n" +
			"  n.nspname = '" + schemaName + "' \n" +
			"  and \n" +
			"  c.relkind = 'r' \n" +
			"order by c.relname";
		return sql;
	}

}
