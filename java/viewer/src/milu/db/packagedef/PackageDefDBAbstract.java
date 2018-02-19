package milu.db.packagedef;

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

public abstract class PackageDefDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Package Definition List
	protected List<Map<String,String>>  packageDefLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.packageDefLst.clear();
	}	
	
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  packageDefEntityLst = new ArrayList<>();
		for ( Map<String,String> packageDef : this.packageDefLst )
		{
			SchemaEntity eachPackageDefEntity = SchemaEntityFactory.createInstance( packageDef.get("packageDefName"), SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF );
			String strStatus = packageDef.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachPackageDefEntity.setState( SchemaEntity.STATE.INVALID );
			}
			packageDefEntityLst.add( eachPackageDefEntity );
		}
		return packageDefEntityLst;
	}	
	
	public void selectPackageDefLst( String schemaName ) throws SQLException
	{
		String sql = this.listSQL( schemaName );
		
		System.out.println( " -- selectPackageDefLst --------------" );
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
				dataRow.put( "packageDefName", rs.getString("object_name") );
				dataRow.put( "status"        , rs.getString("status") );
				this.packageDefLst.add( dataRow );
			}
		}
	}
	
	abstract protected String listSQL( String schemaName );		
}
