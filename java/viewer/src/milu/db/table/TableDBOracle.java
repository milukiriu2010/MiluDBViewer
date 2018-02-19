package milu.db.table;

public class TableDBOracle extends TableDBAbstract 
{

	@Override
	protected String tableLstSQL( String schemaName )
	{
		String sql = 
			" select object_name, status from all_objects \n" +
			" where \n" +
			" object_type='TABLE'      \n" +
			" and \n" +
			" owner = '" + schemaName + "' \n" +
			" order by owner, object_name";
		return sql;
	}

}
