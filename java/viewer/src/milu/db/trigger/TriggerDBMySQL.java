package milu.db.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TriggerDBMySQL extends TriggerDBAbstract 
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
				mapView.put( "triggerName", rs.getString("trigger_name") );
				this.triggerLst.add( mapView );
				*/
				SchemaEntity triggerEntity = SchemaEntityFactory.createInstance( rs.getString("trigger_name"), SchemaEntity.SCHEMA_TYPE.TRIGGER );
				triggerEntityLst.add( triggerEntity );
			}
		}
		
		return triggerEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			" select distinct \n" +
			"   trigger_name     \n"       +
			" from   \n"       +
			"   information_schema.triggers \n" + 
			" where \n"        + 
			"   trigger_schema = '" + schemaName + "' \n" +
			" order by trigger_name";
		return sql;
	}
	
	// Source of Trigger
	@Override
	public String getSRC( String schemaName, String triggerName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DELIMITER //\n" +
				"DROP TRIGGER IF EXISTS " + triggerName + "//\n" +
				"CREATE TRIGGER \n" + triggerName + " \n"
			);
		
		String sql = 
			" select \n" +
			"   action_timing,      \n" +
			"   event_manipulation, \n" +
			"   event_object_table, \n" +
			"   action_statement    \n" +
			" from   \n" +
			"   information_schema.triggers \n" +
			" where  \n" +
			"   trigger_schema = '" + schemaName   + "' \n" +
			"   and  \n" +
			"   trigger_name  = '"  + triggerName + "'";
		
		System.out.println( " -- getSRC(Trigger) -----------" );
		System.out.println( sql );
		System.out.println( " ------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			// AFTER
			src.append( rs.getString("action_timing")      + " " );
			// INSERT
			src.append( rs.getString("event_manipulation") + " ON " );
			src.append( rs.getString("event_object_table") + " \n" );
			src.append( "FOR EACH ROW \n" );
			src.append( rs.getString("action_statement") + " \n" );
			src.append( "//\n" );
			src.append( "\n" );
			src.append( "DELIMITER ;" );
		}
		return src.toString();
	}
}
