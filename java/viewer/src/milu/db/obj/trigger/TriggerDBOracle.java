package milu.db.obj.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TriggerDBOracle extends TriggerDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  triggerEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(Trigger) ------" );
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
				/*
				Map<String, String> mapView = new HashMap<String,String>();
				mapView.put( "triggerName", rs.getString("object_name") );
				mapView.put( "status"  , rs.getString("status") );
				this.triggerLst.add( mapView );
				*/
				SchemaEntity triggerEntity = SchemaEntityFactory.createInstance( rs.getString("object_name"), SchemaEntity.SCHEMA_TYPE.TRIGGER );
				String strStatus = rs.getString("status");
				if ( strStatus != null && "INVALID".equals(strStatus) )
				{
					triggerEntity.setState( SchemaEntity.STATE.INVALID );
				}
				triggerEntityLst.add( triggerEntity );
			}
		}
		
		return triggerEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n"         +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schemaName + "' \n" +
			"   and \n"          +
			"   object_type = 'TRIGGER' \n"  +
			" order by object_name";
		return sql;
	}
	
	// Source of Trigger
	@Override
	public String getSRC( String schemaName, String triggerName ) throws SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schemaName  + "' \n" +
			"   and  \n" +
			"   name  = '" + triggerName + "' \n" +
			"   and  \n" +
			"   type  = 'TRIGGER' \n"  +
			" order by line";
		
		System.out.println( " -- getSRC(Trigger) -----------" );
		System.out.println( sql );
		System.out.println( " ------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		stmt.close();
		rs.close();
		return src.toString();
	}

}
