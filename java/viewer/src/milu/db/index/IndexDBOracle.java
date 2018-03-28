package milu.db.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexDBOracle extends IndexDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName, String tableName) throws SQLException 
	{
		this.clear();

		String sql = this.listSQL( schemaName, tableName );
		System.out.println( " -- selectIndexLst ----------------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------" );
		
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				Map<String, String> dataRow = new HashMap<String,String>();
				dataRow.put( "indexName"    , rs.getString("index_name") );
				dataRow.put( "is_unique"    , rs.getString("is_unique") );
				dataRow.put( "is_primary"   , rs.getString("is_primary") );
				dataRow.put( "is_functional", rs.getString("is_functional") );
				dataRow.put( "status"       , rs.getString("status") );
				this.indexLst.add( dataRow );
			}
		}
		
		return this.getEntityLst();
	}

	@Override
	protected String listSQL( String schemaName, String tableName ) 
	{
		String sql = 
			" select distinct \n"                            +
			"   ai.index_name             index_name, \n"    +
			"   case ai.uniqueness \n"                       +
			"     when 'UNIQUE' then 't' \n"                 +
			"     else               'f' \n"                 +
			"   end                       is_unique,  \n"    +
			"   case ac.constraint_type  \n"                 +
			"     when 'P'      then 't' \n"                 +
			"     else               'f' \n"                 +
			"   end                       is_primary, \n"    +
			"   case ai.index_type       \n"                 +
			"     when 'FUNCTION-BASED NORMAL' then 't' \n"  +
			"     else                              'f' \n"  + 
			"   end                       is_functional, \n" +
			"   case ai.status \n"  +
			"     when  'VALID'  then 'VALID'   \n" +
			"     else                'INVALID' \n" +
			"   end status    \n"  +
			" from \n"  + 
			"   all_indexes      ai,  \n" +
			"   all_ind_columns  aic, \n" +
			"   all_constraints  ac   \n" +
			" where \n" +
			"   ai.owner = '" + schemaName + "' \n"        +
			"   and \n"                                    +
			"   ai.table_name = '" + tableName + "' \n"    +
			"   and \n"                                    +
			"   ai.owner = aic.index_owner \n"             +
			"   and \n"                                    +
			"   ai.table_name = aic.table_name \n"         +
			"   and \n"                                    +
			"   ai.index_name = aic.index_name \n"         +
			"   and \n"                                    +
			"   ai.owner = ac.owner(+) \n"                 +
			"   and \n"                                    +
			"   ai.index_name = ac.constraint_name(+) \n " +
			" order by ai.index_name";
				
		return sql;
	}

}
