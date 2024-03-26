package milu.db.obj.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexDBMySQL extends IndexDBAbstract 
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
				dataRow.put( "is_functional", rs.getString("is_functional") );
				this.indexLst.add( dataRow );
			}
		}
		
		return this.getEntityLst();
	}

	@Override
	protected String listSQL(String schemaName, String tableName) 
	{
		String sql = 
			" select distinct \n"             + 
			"   index_name, \n"               +
			"   seq_in_index, \n"             +
			"   case index_name \n "          +
			"     when 'PRIMARY' then 't' \n" +
			"     else                'f' \n" +
			"   end  is_primary,  \n"         +
			"   case non_unique   \n"         +
			"     when 0 then 't' \n"         +
			"     when 1 then 'f' \n"         +
			"   end  is_unique,   \n"         +
			"   case \n"                      +
			"     when sub_part is not null then 't' \n"  +
			"     else                           'f' \n"  +
			"   end is_functional  \n"       +
			" from \n" +
			"   information_schema.statistics \n" +
			" where \n " +
			"   table_schema = '" + schemaName + "' \n" +
			"   and \n" +
			"   table_name = '" + tableName + "' \n" +
			" order by index_name,seq_in_index";
			
		return sql;
	}

}
