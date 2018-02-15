package milu.db.schema;

import milu.db.MyDBAbstract;

public class SchemaDBMySQL extends SchemaDBAbstract 
{
	public SchemaDBMySQL( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}
	
	@Override
	protected String schemaLstSQL() 
	{
		String sql = "select schema_name from information_schema.schemata order by schema_name";
		return sql;
	}

}
