package milu.db.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TypeDBPostgres extends TypeDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  typeEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(Type) ---------" );
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
				SchemaEntity typeEntity = SchemaEntityFactory.createInstance( rs.getString("user_defined_type_name"), SchemaEntity.SCHEMA_TYPE.TYPE );
				typeEntityLst.add( typeEntity );
			}
		}
		
		return typeEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			" select \n" +
			"   user_defined_type_name    \n" +
			" from  \n"  +
			"   information_schema.user_defined_types \n" + 
			" where \n"  + 
			"   user_defined_type_schema = '" + schemaName + "' \n" +
			" order by user_defined_type_schema, user_defined_type_name";
		return sql;
	}

	// Source of Type
	@Override
	public String getSRC( String schemaName, String typeName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DROP TYPE IF EXISTS " + typeName + ";\n " + 
				"CREATE TYPE " + typeName + " AS " 
			);
		
		String sql = 
			" select \n" + 
			"   a.attname  attname, \n" +
			"   t.typname  typname  \n" +
			" from \n" +  
			"   pg_class c join \n" + 
			"   pg_attribute a on c.oid = a.attrelid join \n" +
			"   pg_type t ON a.atttypid = t.oid \n" +
			" where \n" +
			"   c.relname = '" + typeName + "'";
		
		System.out.println( " -- getSRC(Type) --------------" );
		System.out.println( sql );
		System.out.println( " ------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		int i = 0;
		src.append( "(" );
		while ( rs.next() )
		{
			if ( i != 0 )
			{
				src.append( "," );
			}
			src.append( rs.getString("attname") + " " + rs.getString("typname") );
			i++;
		}
		src.append( ");" );
		return src.toString();
	}
}
