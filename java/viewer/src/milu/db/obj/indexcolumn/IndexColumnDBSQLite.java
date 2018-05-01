package milu.db.obj.indexcolumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexColumnDBSQLite extends IndexColumnDBAbstract 
{
	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName, String tableName, String indexName) 
		throws SQLException 
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
				Map<String, String> mapColumn = new HashMap<String,String>();
				mapColumn.put( "columnName", rs.getString("name") );
				this.indexColumnLst.add( mapColumn );
			}
			return this.getEntityLst();
		}
	}

	@Override
	protected String listSQL(String schemaName, String tableName, String indexName) 
	{
		String sql =
			"pragma index_info(" + indexName + ")";
		return sql;
	}

}
