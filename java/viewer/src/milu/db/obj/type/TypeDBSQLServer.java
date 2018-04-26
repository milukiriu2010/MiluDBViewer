package milu.db.obj.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TypeDBSQLServer extends TypeDBAbstract 
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
				SchemaEntity typeEntity = SchemaEntityFactory.createInstance( rs.getString("name"), SchemaEntity.SCHEMA_TYPE.TYPE );
				typeEntityLst.add( typeEntity );
			}
		}
		
		return typeEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			"select \n" + 
			"  st.name name \n" + 
			"from \n" + 
			"  sys.types st \n" + 
			"  inner join sys.schemas ss on st.schema_id = ss.schema_id \n" + 
			"where \n" + 
			"  st.is_user_defined = 1 \n" + 
			"  and \n" + 
			"  ss.name = '" + schemaName +"'" + 
			"order by st.name";
		return sql;
	}

	// Source of Type
	@Override
	public String getSRC( String schemaName, String typeName ) throws SQLException
	{
		// sp_help 'type name'
		// --------------------------------------
		// maybe the same as sys.types 
		//throw new UnsupportedOperationException();
		return "Unsupported. I'm not sure how to get ddl of user type.";
	}
}
