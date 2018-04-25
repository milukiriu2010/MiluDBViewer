package milu.db.obj.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexDBSQLServer extends IndexDBAbstract 
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
				this.indexLst.add( dataRow );
			}
		}
		
		return this.getEntityLst();
	}

	@Override
	protected String listSQL(String schemaName, String tableName) 
	{
		String sql = 
			"select \n" + 
			"  si.name  index_name, \n" +
			"  case si.is_primary_key \n" + 
			"    when 1 then 't' \n" + 
			"    else        'f' \n" + 
			"  end is_primary,   \n" + 
			"  case si.is_unique \n" +
			"    when 1 then 't' \n" + 
			"    else        'f' \n" + 
			"  end is_unique \n" +
			"from \n" + 
			"   sys.indexes si \n" + 
			"   inner join sys.tables st on si.object_id = st.object_id   \n" + 
			"   inner join sys.schemas ss on st.schema_id = ss.schema_id  \n" + 
			"where \n" + 
			"  si.type_desc <> N'HEAP' \n" + 
			"  and \n" + 
			"  st.name = '"+ tableName + "' \n" + 
			"  and \n" + 
			"  ss.name = '" + schemaName + "' \n" + 
			"order by si.name";
		return sql;
	}

}
