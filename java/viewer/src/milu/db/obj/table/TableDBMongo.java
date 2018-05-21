package milu.db.obj.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TableDBMongo extends TableDBAbstract 
{
	// select Table List
	@Override
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException
	{
		List<SchemaEntity>  tableEntityLst = new ArrayList<>();
		
		DatabaseMetaData md = this.myDBAbs.getMetaData();
		
		try
		(
			ResultSet rs   = md.getTables( null, null, "%", new String[] {"TABLE"} );
		)
		{
			while ( rs.next() )
			{
				SchemaEntity tableEntity = SchemaEntityFactory.createInstance( rs.getString("TABLE_NAME"), SchemaEntity.SCHEMA_TYPE.TABLE );
				tableEntityLst.add( tableEntity );
			}
		}
		
		return tableEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		return null;
	}

	@Override
	public List<Map<String, String>> selectDefinition(String schemaName, String tableName) throws SQLException 
	{
		List<Map<String,String>> dataLst = new ArrayList<>();
		
		String sql = this.definitionSQL( schemaName, tableName );
		
		System.out.println( " -- selectDefinition(Table) -------------" );
		System.out.println( sql );
		System.out.println( " ---------------------------------------------------" );
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs = stmt.executeQuery( sql );
		)
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			int headCnt = rsmd.getColumnCount();
			System.out.println( "---DB COLUMN----------------" );
			for ( int i = 1; i <= headCnt; i++ )
			{
				Map<String,String> dataRow = new LinkedHashMap<String,String>();
				dataRow.put( "column_name"   , rsmd.getColumnName(i) );
				dataRow.put( "data_type"     , rsmd.getColumnTypeName(i) );
				dataRow.put( "data_size"     , String.valueOf(rsmd.getPrecision(i)) );
				String nullAble = "NULL NG";
				if (rsmd.isNullable(i) == ResultSetMetaData.columnNullable)
				{
					nullAble = "NULL OK";
				}
				dataRow.put( "nullable"      , nullAble );
				dataRow.put( "data_default"  , "" );
				dataLst.add( dataRow );
			}			
		}
		return dataLst;
	}

	@Override
	protected String definitionSQL(String schemaName, String tableName) 
	{
		String sql = 
				" select * from " + tableName; 
		return sql;
	}

}
