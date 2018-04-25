package milu.db.obj.schema;

public class SchemaDBSQLServer extends SchemaDBAbstract 
{
	@Override
	protected String schemaLstSQL() 
	{
		String sql = "select name from sys.schemas order by name";
		return sql;
	}

}
