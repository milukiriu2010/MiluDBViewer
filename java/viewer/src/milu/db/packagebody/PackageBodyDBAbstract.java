package milu.db.packagebody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class PackageBodyDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Package Definition List
	protected List<Map<String,String>>  packageBodyLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.packageBodyLst.clear();
	}	
	
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  packageBodyEntityLst = new ArrayList<>();
		for ( Map<String,String> packageBody : this.packageBodyLst )
		{
			SchemaEntity eachPackageBodyEntity = SchemaEntityFactory.createInstance( packageBody.get("packageBodyName"), SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY );
			String strStatus = packageBody.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachPackageBodyEntity.setState( SchemaEntity.STATE.INVALID );
			}
			packageBodyEntityLst.add( eachPackageBodyEntity );
		}
		return packageBodyEntityLst;
	}	
	
	public void selectEntityLst( String schemaName ) throws SQLException
	{
		String sql = this.listSQL( schemaName );
		
		System.out.println( " -- selectPackageBodyLst --------------" );
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
				dataRow.put( "packageBodyName", rs.getString("object_name") );
				dataRow.put( "status"         , rs.getString("status") );
				this.packageBodyLst.add( dataRow );
			}
		}
	}
	
	abstract protected String listSQL( String schemaName );		
}
