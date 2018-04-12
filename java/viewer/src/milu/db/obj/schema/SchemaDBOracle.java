package milu.db.obj.schema;

public class SchemaDBOracle extends SchemaDBAbstract 
{
	@Override
	protected String schemaLstSQL()
	{
		//String sql = "select username from all_users order by username";
		String sql = "select distinct owner from all_objects order by owner";
		return sql;
	}
	
}
