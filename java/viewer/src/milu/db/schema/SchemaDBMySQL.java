package milu.db.schema;

public class SchemaDBMySQL extends SchemaDBAbstract 
{
	@Override
	protected String schemaLstSQL() 
	{
		String sql = "select schema_name from information_schema.schemata order by schema_name";
		return sql;
	}

}
