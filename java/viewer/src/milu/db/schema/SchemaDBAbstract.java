package milu.db.schema;

import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import milu.db.MyDBAbstract;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class SchemaDBAbstract implements ObjDBInterface
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	@Override
	public List<SchemaEntity> selectEntityLst( String tmp ) throws SQLException
	{
		List<SchemaEntity>  schemaEntityLst = new ArrayList<>();
		
		String sql = this.schemaLstSQL();
		
		System.out.println( " -- selectSchemaLst ------------------" );
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
				/*
				Map<String,String> dataRow = new HashMap<>();
				dataRow.put( "schemaName", rs.getString(1) );
				this.schemaNameLst.add( dataRow );
				*/
				
				SchemaEntity eachSchemaEntity = SchemaEntityFactory.createInstance( rs.getString(1), SchemaEntity.SCHEMA_TYPE.SCHEMA );
				schemaEntityLst.add( eachSchemaEntity );
			}
			
			return schemaEntityLst;
		}
	}
	
	abstract protected String schemaLstSQL();
	
	@Override
	public List<Map<String,String>> selectDefinition( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
}
