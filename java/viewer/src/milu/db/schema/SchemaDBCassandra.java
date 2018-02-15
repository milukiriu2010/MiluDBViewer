package milu.db.schema;

import milu.db.MyDBAbstract;

public class SchemaDBCassandra extends SchemaDBAbstract 
{
	public SchemaDBCassandra( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}
	
	@Override
	protected String schemaLstSQL() 
	{
		String sql = "select keyspace_name, durable_writes from system_schema.keyspaces";
		return sql;
	}

}
