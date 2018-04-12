package milu.db.obj.schema;


public class SchemaDBCassandra extends SchemaDBAbstract 
{
	@Override
	protected String schemaLstSQL() 
	{
		String sql = "select keyspace_name, durable_writes from system_schema.keyspaces";
		return sql;
	}

}
