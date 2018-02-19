package milu.db.table;

public class TableDBCassandra extends TableDBAbstract 
{

	@Override
	protected String tableLstSQL(String schemaName) 
	{
		String sql =
			" select \n"           +
			"   table_name     \n" +
			" from \n"             +
			"   system_schema.tables \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			" order by table_name";
		return sql;
	}

}
