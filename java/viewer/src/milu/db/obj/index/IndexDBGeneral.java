package milu.db.obj.index;

import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexDBGeneral extends IndexDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName, String tableName) throws SQLException 
	{
		this.clear();
		
		DatabaseMetaData dm = this.myDBAbs.getMetaData();
		try
		(
			ResultSet        rs = dm.getIndexInfo( null, schemaName, tableName, false, false );
		)
		{
			String oldIndexName = "";
			while ( rs.next() )
			{
				Map<String, String> dataRow = new HashMap<String,String>();
				String indexName = rs.getString("INDEX_NAME");
				if ( indexName.equals(oldIndexName) )
				{
					continue;
				}
				oldIndexName = indexName;
				dataRow.put( "indexName"    , indexName );
				System.out.println( rs.getString("INDEX_NAME") + ":" + rs.getString("NON_UNIQUE") );
				boolean is_unique = rs.getBoolean("NON_UNIQUE");
				dataRow.put( "is_unique"    , ( is_unique ) ? "t":"f" );
				//boolean is_primary = this.isPrimaryKey(schemaName, tableName, indexName);
				//dataRow.put( "is_primary"   , ( is_primary ) ? "t":"f" );
				//dataRow.put( "is_functional", rs.getString("is_functional") );
				//dataRow.put( "status"       , rs.getString("status") );
				this.indexLst.add( dataRow );
			}
		}
		
		return this.getEntityLst();
	}

	@Override
	protected String listSQL(String schemaName, String tableName) 
	{
		return null;
	}

	/*
	 * different result, so comment out
	 * 
	 * h2
	 *   PRIMARYK_KEY_8 <=> CONSTRAINT_8
	private boolean isPrimaryKey( String schemaName, String tableName, String indexName ) throws SQLException
	{
		boolean isPrimaryKey = false;
		DatabaseMetaData dm = this.myDBAbs.getMetaData();
		try
		(
			ResultSet        rs = dm.getPrimaryKeys( null, schemaName, tableName );
		)
		{
			while ( rs.next() )
			{
				String pkName = rs.getString("PK_NAME");
				System.out.println( "PrimaryKey:" + pkName );
				if ( indexName.equals(pkName) )
				{
					isPrimaryKey = true;
					break;
				}
			}
		}		
		return isPrimaryKey;
	}
	 */
}
