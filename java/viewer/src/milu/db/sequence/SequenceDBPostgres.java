package milu.db.sequence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class SequenceDBPostgres extends SequenceDBAbstract 
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
			"  c.relname  object_name \n" +
			"from \n" +
			"  pg_class c join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n" +
			"where \n" +
			"  n.nspname = '" + schemaName + "' \n" +
			"  and \n" +
			"  c.relkind = 'S' \n" +
			"order by c.relname";
		return sql;
	}

}
