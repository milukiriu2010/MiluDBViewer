package milu.db.obj.fk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.entity.schema.SchemaEntityFactory;

public class FKDBSQLServer extends FKDBAbstract
{
	/**
	 * select Foreign Key List
	 */
	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException
	{
		List<SchemaEntity>  fkEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(FK) ---------" );
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
				SchemaEntity seEntity = SchemaEntityFactory.createInstance( rs.getString("constraint_name"), SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
				SchemaEntityEachFK fkEntity = (SchemaEntityEachFK)seEntity;
				fkEntity.setSrcFKSchema( rs.getString("constraint_schema") );
				fkEntity.setSrcConstraintName( rs.getString("constraint_name") );
				fkEntity.setSrcTableSchema( rs.getString("table_schema") );
				fkEntity.setSrcTableName( rs.getString("table_name") );
				fkEntity.setDstTableSchema( rs.getString("referenced_table_schema") );
				fkEntity.setDstTableName( rs.getString("referenced_table_name") );
				fkEntityLst.add( fkEntity );
			}
		}
		
		return fkEntityLst;
	}
	

	@Override
	protected String listSQL(String schemaName)
	{
		String sql = 
			"select \n" + 
			"  ssf.name   constraint_schema, \n" + 
			"  sof.name   constraint_name,   \n" + 
			"  sst1.name  table_schema,      \n" + 
			"  st1.name   table_name,        \n" + 
			"  sst2.name  referenced_table_schema, \n" + 
			"  st2.name   referenced_table_name    \n" + 
			"from \n" + 
			"  sys.foreign_key_columns sf \n" + 
			"  inner join sys.objects sof on sf.constraint_object_id = sof.object_id   \n" + 
			"  inner join sys.schemas ssf on sof.schema_id  = ssf.schema_id            \n" + 
			"  inner join sys.tables st1 on st1.object_id   = sf.parent_object_id      \n" + 
			"  inner join sys.schemas sst1 on st1.schema_id = sst1.schema_id           \n" + 
			"  inner join sys.tables st2 on st2.object_id   = sf.referenced_object_id  \n" + 
			"  inner join sys.schemas sst2 on st2.schema_id = sst2.schema_id           \n" + 
			"where \n" + 
			"  ssf.name = '" + schemaName + "' \n" +
		    "order by ssf.name, sof.name";
		return sql;
	}

	@Override
	public Map<String, String> selectSrcColumnMap( SchemaEntityEachFK fkEntity ) throws SQLException
	{
		String schemaName     = fkEntity.getSrcFKSchema();
		String constraintName = fkEntity.getSrcConstraintName();
		String tableSchema    = fkEntity.getSrcTableSchema();
		String tableName      = fkEntity.getSrcTableName();
		
		String sql = 
			"select \n" + 
			"  col1.name  column_name \n" + 
			"from   \n" + 
			"  sys.foreign_key_columns sf  \n" + 
			"  inner join sys.objects sof  on sf.constraint_object_id = sof.object_id \n" + 
			"  inner join sys.schemas ssf  on sof.schema_id = ssf.schema_id \n" + 
			"  inner join sys.tables  st1  on st1.object_id = sf.parent_object_id \n" + 
			"  inner join sys.schemas sst1 on st1.schema_id = sst1.schema_id \n" + 
			"  inner join sys.columns col1 on ( col1.object_id = sf.parent_object_id and col1.column_id = sf.parent_column_id ) \n" + 
			"where \n" +
			"  ssf.name = '" + schemaName + "' \n" +
			"  and \n" +
			"  sof.name = '" + constraintName + "' \n" +
			"  and \n" +
			"  sst1.name = '" + tableSchema + "' \n" +
			"  and \n" +
			"  st1.name = '" + tableName + "' \n" +
			"order by col1.name";
		
		System.out.println( " -- selectSrcColumnMap(FK) ---------" );
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
				fkEntity.addSrcColumnMap( rs.getString("column_name"), "1" );
			}
		}
		
		return fkEntity.getSrcColumnMap();
	}

	@Override
	public Map<String, String> selectDstColumnMap( SchemaEntityEachFK fkEntity ) throws SQLException
	{
		String schemaName     = fkEntity.getSrcFKSchema();
		String constraintName = fkEntity.getSrcConstraintName();
		String tableSchema    = fkEntity.getDstTableSchema();
		String tableName      = fkEntity.getDstTableName();
		
		String sql = 
			"select \n" + 
			"  col1.name  referenced_column_name \n" + 
			"from   \n" + 
			"  sys.foreign_key_columns sf  \n" + 
			"  inner join sys.objects sof  on sf.constraint_object_id = sof.object_id \n" + 
			"  inner join sys.schemas ssf  on sof.schema_id = ssf.schema_id \n" + 
			"  inner join sys.tables  st1  on st1.object_id = sf.referenced_object_id \n" + 
			"  inner join sys.schemas sst1 on st1.schema_id = sst1.schema_id \n" + 
			"  inner join sys.columns col1 on ( col1.object_id = sf.referenced_object_id and col1.column_id = sf.referenced_column_id ) \n" + 
			"where \n" +
			"  ssf.name = '" + schemaName + "' \n" +
			"  and \n" +
			"  sof.name = '" + constraintName + "' \n" +
			"  and \n" +
			"  sst1.name = '" + tableSchema + "' \n" +
			"  and \n" +
			"  st1.name = '" + tableName + "' \n" +
			"order by col1.name";
		
		System.out.println( " -- selectDstColumnMap(FK) ---------" );
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
				fkEntity.addDstColumnMap( rs.getString("referenced_column_name"), "1" );
			}
		}
		
		return fkEntity.getDstColumnMap();
	}
	
	@Override
	public List<Map<String,String>> selectDefinition( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
}
