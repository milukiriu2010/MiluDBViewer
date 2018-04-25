package milu.db.obj.indexcolumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexColumnDBSQLServer extends IndexColumnDBAbstract 
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
			"  sc.name  column_name \n" + 
			"from \n" + 
			"   sys.indexes si \n" + 
			"   inner join sys.tables st on si.object_id = st.object_id   \n" + 
			"   inner join sys.schemas ss on st.schema_id = ss.schema_id  \n" + 
			"   inner join sys.index_columns sic on ( sic.object_id = si.object_id and sic.index_id = si.index_id )\n" +
			"   inner join sys.columns sc on ( sc.object_id = sic.object_id and sc.column_id = sic.column_id )\n" + 
			"where\n" + 
			"  si.name = '" + indexName + "'\n" + 
			"  and \n" + 
			"  st.name = '"+ tableName + "' \n" + 
			"  and \n" + 
			"  ss.name = '" + schemaName + "' \n" + 
			"order by sic.index_id";
		return sql;
	}

}
