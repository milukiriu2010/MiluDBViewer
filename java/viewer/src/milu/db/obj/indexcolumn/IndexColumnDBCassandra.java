package milu.db.obj.indexcolumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexColumnDBCassandra extends IndexColumnDBAbstract
{
	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName, String tableName, String indexName) throws SQLException 
	{
		this.clear();
		
		String sql = this.listSQL( schemaName, tableName, indexName );
		System.out.println( " -- selectIndexColumnLst ----------" );
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
				String kind     = rs.getString( "kind" );
				if ( indexName.equals( kind ) == false )
				{
					continue;
				}
				
				String position = rs.getString( "position" );
				int pos = Integer.parseInt( position );
				
				Map<String, String> mapColumn = new HashMap<String,String>();
				mapColumn.put( "columnName"     , rs.getString( "column_name" ) );
				mapColumn.put( "clusteringOrder", rs.getString( "clustering_order" ) );
				if ( pos > this.indexColumnLst.size() )
				{
					pos = this.indexColumnLst.size();
				}
				this.indexColumnLst.add( pos, mapColumn );
			}
			
			return this.getEntityLst();
		}
	}

	@Override
	protected String listSQL(String schemaName, String tableName, String indexName) 
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
