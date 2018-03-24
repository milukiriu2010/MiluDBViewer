package milu.db.fk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.entity.schema.SchemaEntityFactory;

public class FKDBPostgres extends FKDBAbstract 
{

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
				SchemaEntity seEntity = SchemaEntityFactory.createInstance( rs.getString("acs_constraint_name"), SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
				SchemaEntityEachFK fkEntity = (SchemaEntityEachFK)seEntity;
				fkEntity.setSrcFKSchema( rs.getString("acs_constraint_schema") );
				fkEntity.setSrcConstraintName( rs.getString("acs_constraint_name") );
				fkEntity.setSrcTableSchema( rs.getString("acs_table_schema") );
				fkEntity.setSrcTableName( rs.getString("acs_table_name") );
				fkEntity.setDstConstraintName( rs.getString("acd_constraint_name") );
				fkEntity.setDstTableSchema( rs.getString("acd_table_schema") );
				fkEntity.setDstTableName( rs.getString("acd_table_name") );
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
			"  acs1.constraint_schema         acs_constraint_schema, \n" + // src fk schema
			"  acs1.constraint_name           acs_constraint_name, \n"   + // src fk name
			"  acs1.table_schema              acs_table_schema, \n"      + // src table schema
			"  acs1.table_name                acs_table_name, \n"        + // src table name
			"  acs2.unique_constraint_schema  acd_constraint_schema, \n" + // dst constraint schema
			"  acs2.unique_constraint_name    acd_constraint_name, \n"   + // dst constraint name
			"  acd.table_schema               acd_table_schema, \n"      + // dst table schema
			"  acd.table_name                 acd_table_name \n"         + // dst table name
			"from \n" + 
			"  information_schema.table_constraints  acs1 \n"        +
			"  left join \n" + 
			"  information_schema.referential_constraints acs2 \n"   +
			"  on \n"  +
			"  acs1.constraint_catalog = acs2.constraint_catalog \n" +
			"  and \n" +
			"  acs1.constraint_schema = acs2.constraint_schema \n"   +
			"  and \n" +
			"  acs1.constraint_name = acs2.constraint_name \n"       +
			"  left join \n" +
			"  information_schema.table_constraints  acd \n"         +
			"  on \n"  +
			"  acs2.unique_constraint_catalog = acd.constraint_catalog \n"  +
			"  and \n" +
			"  acs2.unique_constraint_schema   = acd.constraint_schema \n"  +
			"  and \n" +
			"  acs2.unique_constraint_name = acd.constraint_name \n"        +
			"where \n" +
			"  acs1.constraint_schema = '" + schemaName + "' \n" +
			"  and \n" +
			"  acs1.constraint_type = 'FOREIGN KEY' \n" +
			"order by acs1.constraint_name";				
				
			return sql;
	}

	@Override
	public Map<String, String> selectSrcColumnMap(SchemaEntityEachFK fkEntity) throws SQLException 
	{
		String schemaName     = fkEntity.getSrcFKSchema();
		String constraintName = fkEntity.getSrcConstraintName();
		String tableSchema    = fkEntity.getSrcTableSchema();
		String tableName      = fkEntity.getSrcTableName();
		
		String sql = this.listColumnSQL(schemaName, constraintName, tableSchema, tableName );
		
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
				fkEntity.addSrcColumnMap( rs.getString("column_name"), rs.getString("ordinal_position") );
			}
		}
		
		return fkEntity.getSrcColumnMap();
	}

	@Override
	public Map<String, String> selectDstColumnMap(SchemaEntityEachFK fkEntity) throws SQLException 
	{
		String schemaName     = fkEntity.getSrcFKSchema();
		String constraintName = fkEntity.getDstConstraintName();
		String tableSchema    = fkEntity.getDstTableSchema();
		String tableName      = fkEntity.getDstTableName();
		
		String sql = this.listColumnSQL(schemaName, constraintName, tableSchema, tableName );
		
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
				fkEntity.addDstColumnMap( rs.getString("column_name"), rs.getString("ordinal_position") );
			}
		}
		
		return fkEntity.getDstColumnMap();
	}
	
	private String listColumnSQL( String schemaName, String constraintName, String tableSchema, String tableName )
	{
		String sql = 
			"select \n" +
			"  column_name, \n"     +
			"  ordinal_position \n" +
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
			"order by ordinal_position";
		return sql;
	}

}
