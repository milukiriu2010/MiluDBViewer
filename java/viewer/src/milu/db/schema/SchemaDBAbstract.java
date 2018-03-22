package milu.db.schema;

import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import milu.db.MyDBAbstract;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class SchemaDBAbstract implements ObjDBInterface
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	/*
	// Schema Name List
	protected List<Map<String,String>>  schemaNameLst = new ArrayList<>();
	*/
	
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	/*
	public List<Map<String,String>> getSchemaNameLst()
	{
		return this.schemaNameLst;
	}
	
	public List<SchemaEntity> getSchemaEntityLst()
	{
		List<SchemaEntity>  schemaEntityLst = new ArrayList<>();
		for ( Map<String,String> schemaName : this.schemaNameLst )
		{
			SchemaEntity eachSchemaEntity = SchemaEntityFactory.createInstance( schemaName.get("schemaName"), SchemaEntity.SCHEMA_TYPE.SCHEMA );
			//SchemaEntity rootTableEntity  = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
			//eachSchemaEntity.addEntity( rootTableEntity );
			schemaEntityLst.add( eachSchemaEntity );
		}
		return schemaEntityLst;
	}
	
	protected void clear()
	{
		this.schemaNameLst.clear();
	}
	*/
	
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
}
