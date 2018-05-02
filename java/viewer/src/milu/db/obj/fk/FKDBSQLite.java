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

public class FKDBSQLite extends FKDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<String> tableLst = this.selectTableLst();
		
		List<SchemaEntity>  fkEntityLst = new ArrayList<>();

		for ( String tableName : tableLst )
		{
			String sql = this.listSQL( tableName );
			System.out.println( " -- selectEntityLst(FK) ---------" );
			System.out.println( sql );
			System.out.println( " ----------------------------------" );
			try
			(
				Statement stmt = this.myDBAbs.createStatement();
				ResultSet rs   = stmt.executeQuery( sql );
			)
			{
				int id = 0;
				while ( rs.next() )
				{
					SchemaEntity seEntity = SchemaEntityFactory.createInstance( tableName + "_" + id, SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
					SchemaEntityEachFK fkEntity = (SchemaEntityEachFK)seEntity;
					fkEntity.setSrcFKSchema( null );
					fkEntity.setSrcConstraintName( tableName + "_" + id );
					fkEntity.setSrcTableSchema( null );
					fkEntity.setSrcTableName( tableName );
					fkEntity.setDstConstraintName( null );
					fkEntity.setDstTableSchema( null );
					fkEntity.setDstTableName( rs.getString("table") );
					fkEntityLst.add( fkEntity );
					
					id++;
				}
			}
		}
		
		return fkEntityLst;
	}

	@Override
	protected String listSQL(String tableName) 
	{
		String sql =
			"pragma foreign_key_list(" + tableName + ")";
		return sql;
	}
	
	private List<String> selectTableLst() throws SQLException
	{
		List<String> tableLst = new ArrayList<>();
		String sql = "select name from sqlite_master where type='table'";
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				tableLst.add( rs.getString("name") );
			}
			
			return tableLst;
		}
		
	}

	@Override
	public Map<String, String> selectSrcColumnMap(SchemaEntityEachFK fkEntity) throws SQLException 
	{
		String constraintName = fkEntity.getSrcConstraintName();
		String tableName      = fkEntity.getSrcTableName();
		
		String sql = this.listSQL( tableName );
		
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
				String id = rs.getString("id");
				if ( constraintName.equals( tableName+"_"+id ) )
				{
					fkEntity.addSrcColumnMap( rs.getString("from"), rs.getString("seq") );
				}
			}
		}
		
		return fkEntity.getSrcColumnMap();
	}

	@Override
	public Map<String, String> selectDstColumnMap(SchemaEntityEachFK fkEntity) throws SQLException 
	{
		String constraintName = fkEntity.getSrcConstraintName();
		String srcTableName   = fkEntity.getSrcTableName();
		String dstTableName   = fkEntity.getDstTableName();
		
		String sql = this.listSQL( srcTableName );
		
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
				String id = rs.getString("id");
				if 
				( 
					constraintName.equals( srcTableName+"_"+id ) && 
					dstTableName.equals(rs.getString("table"))
				)
				{
					fkEntity.addDstColumnMap( rs.getString("to"), rs.getString("seq") );
				}
			}
		}
		
		return fkEntity.getDstColumnMap();
	}
}
