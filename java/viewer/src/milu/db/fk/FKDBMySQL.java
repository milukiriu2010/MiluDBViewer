package milu.db.fk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.entity.schema.SchemaEntityFactory;

public class FKDBMySQL extends FKDBAbstract
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
			" select distinct \n" +
			"   constraint_schema, \n"       +  // src fk schema
			"   constraint_name, \n"         +	// src fk name
			"   table_schema, \n"            +  // src table schema
			"   table_name, \n"              +	// src table name
			"   referenced_table_schema, \n" +	// dst table owner
			"   referenced_table_name \n"    +	// dst table name
			" from \n" +
			"   information_schema.key_column_usage \n" +
			" where \n" +
			"   constraint_schema='" + schemaName + "' \n" +
			"   and \n" +
			"   referenced_table_schema is not NULL \n" +
			" order by constraint_name";
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
			"  column_name, \n" +
			"  position_in_unique_constraint \n" +
			"from \n" +
			"  information_schema.key_column_usage \n" +
			"where \n" +
			"  constraint_schema = '" + schemaName + "' \n" +
			"  and \n" +
			"  constraint_name = '" + constraintName + "' \n" +
			"  and \n" +
			"  table_schema = '" + tableSchema + "' \n" +
			"  and \n" +
			"  table_name = '" + tableName + "' \n" +
			"order by position_in_unique_constraint";
		
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
				fkEntity.addSrcColumnMap( rs.getString("column_name"), rs.getString("position_in_unique_constraint") );
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
			"  referenced_column_name, \n" +
			"  position_in_unique_constraint \n" +
			"from \n" +
			"  information_schema.key_column_usage \n" +
			"where \n" +
			"  constraint_schema = '" + schemaName + "' \n" +
			"  and \n" +
			"  constraint_name = '" + constraintName + "' \n" +
			"  and \n" +
			"  referenced_table_schema = '" + tableSchema + "' \n" +
			"  and \n" +
			"  referenced_table_name = '" + tableName + "' \n" +
			"order by position_in_unique_constraint";
		
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
				fkEntity.addDstColumnMap( rs.getString("referenced_column_name"), rs.getString("position_in_unique_constraint") );
			}
		}
		
		return fkEntity.getDstColumnMap();
	}
}
