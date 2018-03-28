package milu.db.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexDBPostgres extends IndexDBAbstract {

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName, String tableName) throws SQLException 
	{
		this.clear();

		String sql = this.listSQL( schemaName, tableName );
		System.out.println( " -- selectIndexLst ----------------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------" );
		
		if ( sql != null )
		{
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
		}
		
		return this.getEntityLst();
	}

	@Override
	protected String listSQL(String schemaName, String tableName) 
	{
		// The following SQL doesn't work well for "information_schema"
		if ( "information_schema".equals( schemaName ) )
		{
			return null;
		}
		
		String sql = 
			" SELECT \n" +
			"   idx.indrelid::REGCLASS   AS table_name, \n"  +
			"   i.relname                AS index_name, \n"  +
			"   idx.indisunique          AS is_unique,  \n"  +
			"   idx.indisprimary         AS is_primary, \n"  +
			"   (idx.indexprs IS NOT NULL) OR \n"                     +
			"   (idx.indkey::int[] @> array[0]) AS is_functional, \n" +
			"   case idx.indisvalid       \n" + 
			"     when 't' then 'VALID'   \n" + 
			"     else          'INVALID' \n" + 
			"   end  status,              \n" + 
			"   idx.indpred IS NOT NULL AS is_partial \n" +
			" FROM \n" +
			"   pg_index          AS idx \n" +
			"   JOIN pg_class     AS i   ON i.oid          = idx.indexrelid \n" +
			"   JOIN pg_namespace AS NS  ON i.relnamespace = NS.OID \n"         +
			" WHERE \n"                          + 
			"   NS.nspname = '" + schemaName + "' \n" +
			"   and \n"                          +
			"   idx.indrelid = '" + tableName +"'::REGCLASS \n" +
			" ORDER BY i.relname";
		
		return sql;
	}

}
