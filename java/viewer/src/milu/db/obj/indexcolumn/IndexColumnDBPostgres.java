package milu.db.obj.indexcolumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

import java.util.HashMap;
import java.util.List;

public class IndexColumnDBPostgres extends IndexColumnDBAbstract
{
	@Override
	public List<SchemaEntity> selectEntityLst( String schemaName, String tableName, String indexName ) throws SQLException
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
				// --------------------------------------------
				// split column list into each column
				// --------------------------------------------
				// ex1. {column1}
				//        => column1
				// ex2. {column1,column2}
				//        => column1 column2
				// ex3. {countrycode,language}
				//        => countrycode, language
				// --------------------------------------------
				String strColumns = rs.getString(1);
				strColumns = strColumns.replace( "{",  "" );
				strColumns = strColumns.replace( "}",  "" );
				String[] arrayColumns = strColumns.split(",");
				for ( int i = 0; i < arrayColumns.length; i++ )
				{
					Map<String, String> mapColumn = new HashMap<String,String>();
					mapColumn.put( "columnName", arrayColumns[i] );
					this.indexColumnLst.add( mapColumn );
				}
			}
			return this.getEntityLst();
		}
	}
	
	/**
	 * 
select
  array
  (
  select
    pg_get_indexdef(i.indexrelid, k+1, TRUE )
  from
    generate_subscripts(i.indkey,1) as k
  order by k
  )
from
  pg_index  i  join
  pg_class  c on c.oid = i.indexrelid  join
  pg_namespace n on n.oid = c.relnamespace
where
  n.nspname = 'public'
  and
  i.indrelid = 'countrylanguage'::REGCLASS
  and
  c.relname = 'countrylanguage_pkey'
	 */
	@Override
	protected String listSQL( String schemaName, String tableName, String indexName )
	{
		String sql =
			"select \n"  +
			"  array \n" +
			"  ( \n"     +
			"    select \n" +
			"      pg_get_indexdef(i.indexrelid, k+1, TRUE ) \n" +
			"    from \n" +
			"      generate_subscripts(i.indkey,1) as k \n" +
			"    order by k \n" +
			"  ) column_lists\n" +
			"from \n" +
			"  pg_index  i  join \n" +
			"  pg_class  c on c.oid = i.indexrelid  join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n"  +
			"where \n" +
			"  n.nspname = '" + schemaName + "' \n" +
			"  and \n" +
			"  i.indrelid = '" + tableName + "'::REGCLASS \n" +
			"  and \n" +
			"  c.relname = '" + indexName + "'";
		return sql;
	}

}
