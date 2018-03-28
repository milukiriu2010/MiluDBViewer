package milu.db.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexDBCassandra extends IndexDBAbstract 
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
			// column name list for "partition_key"
			List<String> partitionColumnLst = new LinkedList<String>();
			
			// column name list for "clustering"
			List<String> clusteringColumnLst = new LinkedList<String>();
			
			while ( rs.next() )
			{
				String kind     = rs.getString( "kind" );
				String position = rs.getString( "position" );
				int pos = Integer.parseInt( position );
				if ( "partition_key".equals(kind) )
				{
					if ( pos > partitionColumnLst.size() )
					{
						pos = partitionColumnLst.size();
					}
					partitionColumnLst.add( pos, rs.getString("column_name") );
				}
				else if ( "clustering".equals(kind) )
				{
					if ( pos > clusteringColumnLst.size() )
					{
						pos = clusteringColumnLst.size();
					}
					clusteringColumnLst.add( pos, rs.getString("column_name") );
				}
			}			
			
			// partition_key
			Map<String,String> partitionKey  = new HashMap<String,String>();
			partitionKey.put( "indexName", "partition_key" );
			partitionKey.put( "is_primary", "t" );
			partitionKey.put( "index_keys", String.join( ",", partitionColumnLst ) );
			this.indexLst.add( partitionKey );

			if ( clusteringColumnLst.size() > 0 )
			{
				Map<String,String> clusteringKey = new HashMap<String,String>();
				clusteringKey.put( "indexName", "clustering" );
				clusteringKey.put( "index_keys", String.join( ",", clusteringColumnLst ) );
				this.indexLst.add( clusteringKey );
			}
			
			return this.getEntityLst();
		}
	}

	@Override
	protected String listSQL(String schemaName, String tableName) 
	{
		String sql =
			" select         \n" +
		    "   column_name, \n" +
		    "   kind,        \n" +
		    "   position,    \n" +
		    "   clustering_order \n" +
			" from           \n" +
			"   system_schema.columns \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			"   and \n" +
			"   table_name = '" + tableName + "'";
		return sql;
	}

}
