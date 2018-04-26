package milu.db.obj.sequence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class SequenceDBSQLServer extends SequenceDBAbstract 
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
				SchemaEntity sequenceEntity = SchemaEntityFactory.createInstance( rs.getString("name"), SchemaEntity.SCHEMA_TYPE.SEQUENCE );
				sequenceEntityLst.add( sequenceEntity );
			}
		}
		
		return sequenceEntityLst;
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
			"  so.type = 'SO' \n" + 
			"  and \n" + 
			"  ss.name = '" + schemaName +"'" + 
			"order by so.name";
		return sql;
	}

}
