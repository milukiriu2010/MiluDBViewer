package milu.db.fk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.entity.schema.SchemaEntityFactory;

public class FKDBOracle extends FKDBAbstract
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
				SchemaEntity seEntity = SchemaEntityFactory.createInstance( rs.getString("acs_constraint_name"), SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
				SchemaEntityEachFK fkEntity = (SchemaEntityEachFK)seEntity;
				fkEntity.setSrcFKSchema( rs.getString("acs_owner") );
				fkEntity.setSrcConstraintName( rs.getString("acs_constraint_name") );
				fkEntity.setSrcTableSchema( rs.getString("acs_owner") );
				fkEntity.setSrcTableName( rs.getString("acs_table_name") );
				fkEntity.setDstConstraintName( rs.getString("acd_constraint_name") );
				fkEntity.setDstTableSchema( rs.getString("acd_owner") );
				fkEntity.setDstTableName( rs.getString("acd_table_name") );
				String strStatus = rs.getString("acs_status");
				if ( strStatus != null && "ENABLED".equals(strStatus) == false )
				{
					fkEntity.setState( SchemaEntity.STATE.INVALID );
				}
				fkEntityLst.add( fkEntity );
			}
		}
		
		return fkEntityLst;
	}
	
	@Override
	protected String listSQL(String schemaName)
	{
		String sql =
			"select  \n" + 
			"  acs.owner            acs_owner, \n"            + // src fk schema & src table schema 
			"  acs.constraint_name  acs_constraint_name,  \n" + // src fk name
			"  acs.table_name       acs_table_name,  \n"      + // src table name
			"  acd.owner            acd_owner,  \n"           + // dst constraint owner & dst table owner
			"  acd.constraint_name  acd_constraint_name,  \n" + // dst constraint name
			"  acd.table_name       acd_table_name,  \n"      + // dst table name
			"  acs.status           acs_status  \n"           + 
			"from  \n" + 
			"  (   \n" + 
			"  select \n"                + 
			"    owner, \n"              + 
			"    constraint_name, \n"    + 
			"    table_name, \n"         + 
			"    r_owner, \n"            + 
			"    r_constraint_name, \n"  + 
			"    status \n"              + 
			"  from \n"                  + 
			"    all_constraints \n"     + 
			"  where \n"                 + 
			"    constraint_type='R' \n" + 
			"    and \n"                 + 
			"    owner = '" + schemaName + "' \n"  + 
			"  ) acs,  \n"               + 
			"  all_constraints acd \n"   + 
			"where \n" + 
			"  acs.r_owner = acd.owner \n" + 
			"  and \n" + 
			"  acs.r_constraint_name = acd.constraint_name  \n" + 
			"order by acs.constraint_name";
		return sql;
	}

	@Override
	public Map<String, String> selectSrcColumnMap( SchemaEntityEachFK fkEntity ) throws SQLException
	{
		String schemaName     = fkEntity.getSrcFKSchema();
		String constraintName = fkEntity.getSrcConstraintName();
		String tableName      = fkEntity.getSrcTableName();
		
		String sql = this.listColumnSQL(schemaName, constraintName, tableName);
		
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
				fkEntity.addSrcColumnMap( rs.getString("column_name"), rs.getString("position") );
			}
		}
		
		return fkEntity.getSrcColumnMap();
	}

	@Override
	public Map<String, String> selectDstColumnMap( SchemaEntityEachFK fkEntity ) throws SQLException
	{
		String schemaName     = fkEntity.getDstTableSchema();
		String constraintName = fkEntity.getDstConstraintName();
		String tableName      = fkEntity.getDstTableName();
		
		String sql = this.listColumnSQL(schemaName, constraintName, tableName);
		
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
				fkEntity.addDstColumnMap( rs.getString("column_name"), rs.getString("position") );
			}
		}
		
		return fkEntity.getDstColumnMap();
	}
	
	private String listColumnSQL( String schemaName, String constraintName, String tableName )
	{
		String sql = 
			"select \n" +
			"  column_name, \n" +
			"  position \n" +
			"from \n" +
			"  all_cons_columns \n" +
			"where \n" +
			"  owner = '" + schemaName + "' \n" +
			"  and \n" +
			"  constraint_name = '" + constraintName + "' \n" +
			"  and \n" +
			"  table_name = '" + tableName + "' \n" +
			"order by position";
		return sql;
	}
}
