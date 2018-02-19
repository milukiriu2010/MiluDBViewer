package milu.db.sequence;

public class SequenceDBPostgres extends SequenceDBAbstract 
{

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			"select \n" +
			"  c.relname  object_name \n" +
			"from \n" +
			"  pg_class c join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n" +
			"where \n" +
			"  n.nspname = '" + schemaName + "' \n" +
			"  and \n" +
			"  c.relkind = 'S' \n" +
			"order by c.relname";
		return sql;
	}

}
