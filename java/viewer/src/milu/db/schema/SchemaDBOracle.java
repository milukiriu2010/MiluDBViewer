package milu.db.schema;

import milu.db.MyDBAbstract;

public class SchemaDBOracle extends SchemaDBAbstract 
{
	public SchemaDBOracle( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	protected String schemaLstSQL()
	{
		String sql = "select username from all_users order by username";
		return sql;
	}
	
}
