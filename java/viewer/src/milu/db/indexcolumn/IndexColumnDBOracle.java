package milu.db.indexcolumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import milu.db.MyDBAbstract;

public class IndexColumnDBOracle extends IndexColumnDBAbstract 
{
	public IndexColumnDBOracle( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	public void selectEntityLst(String schemaName, String tableName, String indexName) 
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
				mapColumn.put( "columnName", rs.getString(1) );
				this.indexColumnLst.add( mapColumn );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName, String tableName, String indexName) 
	{
		String sql = 
			" select \n" + 
			"   aic.column_name  column_name \n" + 
			" from \r\n" + 
			"   all_indexes      ai,  \n" + 
			"   all_ind_columns  aic  \n" + 
			" where \r\n" + 
			"   ai.owner = '" + schemaName + "' \n" + 
			"   and \n" + 
			"   ai.table_name = '" + tableName + "' \n" + 
			"   and \n" + 
			"   ai.index_name = '" + indexName + "' \n" + 
			"   and \n" + 
			"   ai.owner = aic.index_owner \n" + 
			"   and \n" + 
			"   ai.table_name = aic.table_name \n" + 
			"   and \n" + 
			"   ai.index_name = aic.index_name \n" + 
			"order by aic.column_name"; 
		return sql;
	}

}
