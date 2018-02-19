package milu.db.table;

public class TableDBMySQL extends TableDBAbstract 
{

	@Override
	protected String tableLstSQL( String schemaName ) 
	{
		String sql = 
			" select \n" +
			"	table_name   \n" +
			" from \n" +
			"   information_schema.tables \n" +
			" where \n" +
			"   table_type='BASE TABLE' \n" +
			"   and \n" +
			"   table_schema = '" + schemaName + "' \n" +
			" order by table_name";
		return sql;
	}

}
