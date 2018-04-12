package milu.db.obj.indexcolumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexColumnDBMySQL extends IndexColumnDBAbstract 
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
				mapColumn.put( "columnName", rs.getString(1) );
				this.indexColumnLst.add( mapColumn );
			}
			return this.getEntityLst();
		}
	}

	@Override
	protected String listSQL(String schemaName, String tableName, String indexName) 
	{
		String sql =
			"select \n" + 
			"   column_name \n" + 
			" from \n" + 
			"   information_schema.statistics \n" + 
			" where \n" + 
			"    table_schema = '" + schemaName + "' \n" + 
			"    and \n" + 
			"    table_name = '" + tableName + "' \n" +
			"    and \n" +
			"    index_name = '" + indexName + "' \n" +
			" order by table_name,index_name,seq_in_index\r\n" + 
			"";
		return sql;
	}

}
