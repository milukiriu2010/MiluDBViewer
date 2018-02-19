package milu.db.packagedef;

public class PackageDefDBOracle extends PackageDefDBAbstract 
{

	@Override
	protected String listSQL(String schemaName)
	{
		String sql =
			" select \n"         +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schemaName + "' \n" +
			"   and \n"          +
			"   object_type = 'PACKAGE' \n"  +
			" order by object_name";
		return sql;
	}

}
