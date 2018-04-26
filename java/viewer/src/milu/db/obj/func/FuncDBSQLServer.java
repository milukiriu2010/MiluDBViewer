package milu.db.obj.func;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class FuncDBSQLServer extends FuncDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  funcEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(Func) ---------" );
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
				SchemaEntity funcEntity = SchemaEntityFactory.createInstance( rs.getString("name"), SchemaEntity.SCHEMA_TYPE.FUNC );
				funcEntityLst.add( funcEntity );
			}
		}
		
		return funcEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			"select \n" + 
			"  so.name name \n" + 
			"from \n" + 
			"  sys.objects so \n" + 
			"  inner join sys.schemas ss on so.schema_id = ss.schema_id \n" + 
			"where \n" + 
			"  so.type = 'FN' \n" + 
			"  and \n" + 
			"  ss.name = '" + schemaName +"'" + 
			"order by so.name";
		return sql;
	}

	// Source of Function
	@Override
	public String getSRC( String schemaName, String funcName ) throws SQLException
	{
		StringBuffer src = new StringBuffer();
		
		String sql = 
			" select \n" +
			"   object_definition( object_id('" + schemaName + "." + funcName +"') ) src";
		
		System.out.println( " -- getSRC(Func) -----------" );
		System.out.println( sql );
		System.out.println( " ---------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("src") );
		}
		src.append(";");
		return src.toString();
	}
}
