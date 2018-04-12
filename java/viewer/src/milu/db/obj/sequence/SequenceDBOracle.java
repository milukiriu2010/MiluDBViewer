package milu.db.obj.sequence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class SequenceDBOracle extends SequenceDBAbstract
{
	@Override
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException
	{
		List<SchemaEntity>  sequenceEntityLst = new ArrayList<>();
		
		String sql = this.listSQL( schemaName );
		
		System.out.println( " -- selectEntityLst(Sequence) --------" );
		System.out.println( sql );
		System.out.println( " -------------------------------------" );
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql )
		)
		{
			while ( rs.next() )
			{
				SchemaEntity sequenceEntity = SchemaEntityFactory.createInstance( rs.getString("object_name"), SchemaEntity.SCHEMA_TYPE.SEQUENCE );
				String strStatus = rs.getString("status");
				if ( strStatus != null && "INVALID".equals(strStatus) )
				{
					sequenceEntity.setState( SchemaEntity.STATE.INVALID );
				}
				sequenceEntityLst.add( sequenceEntity );
			}
		}
		
		return sequenceEntityLst;
	}
	
	@Override
	protected String listSQL( String schemaName )
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
			"   object_type = 'SEQUENCE' \n"  +
			" order by object_name";
		return sql;
	}
}
