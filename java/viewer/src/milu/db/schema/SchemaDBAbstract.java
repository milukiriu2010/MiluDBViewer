package milu.db.schema;

import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class SchemaDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Schema Name List
	protected List<Map<String,String>>  schemaNameLst = new ArrayList<>();
	
	public SchemaDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
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
	
	public void selectSchemaLst() throws SQLException
	{
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
				Map<String,String> dataRow = new HashMap<>();
				dataRow.put( "schemaName", rs.getString(1) );
				this.schemaNameLst.add( dataRow );
			}
		}
	}
	
	abstract protected String schemaLstSQL();	
}
