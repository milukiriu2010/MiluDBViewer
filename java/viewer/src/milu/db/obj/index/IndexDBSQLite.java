package milu.db.obj.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexDBSQLite extends IndexDBAbstract 
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
				String is_unique = "f";
				String unique    = rs.getString("unique");
				if ( "1".equals(unique) )
				{
					is_unique = "t";
				}
				String is_primary = "f";
				String origin     = rs.getString("origin");
				if ( "pk".equals(origin) )
				{
					is_primary = "t";
				}
				
				dataRow.put( "indexName"    , rs.getString("name") );
				dataRow.put( "is_unique"    , is_unique );
				dataRow.put( "is_primary"   , is_primary );
				this.indexLst.add( dataRow );
			}
		}
		
		return this.getEntityLst();
	}

	@Override
	protected String listSQL(String schemaName, String tableName) 
	{
		String sql = 
			" pragma index_list(" + tableName + ")"; 
		return sql;
	}

}
