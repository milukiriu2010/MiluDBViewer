package milu.db.sequence;

public class SequenceDBOracle extends SequenceDBAbstract
{
	@Override
	protected String listSQL( String schemaName )
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
			"   object_type = 'SEQUENCE' \n"  +
			" order by object_name";
		return sql;
	}
}
