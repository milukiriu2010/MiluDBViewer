package milu.db.obj.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TriggerDBPostgres extends TriggerDBAbstract 
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
				mapView.put( "triggerName", rs.getString("tgname") );
				this.triggerLst.add( mapView );
				*/
				SchemaEntity triggerEntity = SchemaEntityFactory.createInstance( rs.getString("tgname"), SchemaEntity.SCHEMA_TYPE.TRIGGER );
				triggerEntityLst.add( triggerEntity );				
			}
		}
		
		return triggerEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n" +
			"   t.tgname   tgname \n" +
			" from \n"   +
			"   pg_trigger   t join \n" +
			"   pg_class     c on ( t.tgrelid = c.oid ) join \n" +
			"   pg_namespace n on ( c.relnamespace = n.oid ) \n" +
			" where \n" +
			"   n.nspname = '" + schemaName + "' \n" +
			"   and \n" +
			"   t.tgname not like 'RI_ConstraintTrigger%' \n" +
			" order by t.tgname";
		return sql;
	}

	// Source of Trigger
	@Override
	public String getSRC( String schemaName, String triggerName ) throws SQLException
	{
		String src = "";
		
		String sql = "select pg_get_functiondef('" + triggerName + "'::regproc) src";
		
		System.out.println( " -- getSRC(Trigger) -----------" );
		System.out.println( sql );
		System.out.println( " ------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src = rs.getString( "src" );
		}
		return src;
	}
}
