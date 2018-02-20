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
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	/*
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
	*/
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException
	{
		List<SchemaEntity>  packageBodyEntityLst = new ArrayList<>();
		
		String sql = this.listSQL( schemaName );
		
		System.out.println( " -- selectEntityLst(Package Body) -----" );
		System.out.println( sql );
		System.out.println( " --------------------------------------" );
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
				dataRow.put( "packageBodyName", rs.getString("object_name") );
				dataRow.put( "status"         , rs.getString("status") );
				this.packageBodyLst.add( dataRow );
				*/
				SchemaEntity packageBodyEntity = SchemaEntityFactory.createInstance( rs.getString("object_name"), SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY );
				String strStatus = rs.getString("status");
				if ( strStatus != null && "INVALID".equals(strStatus) )
				{
					packageBodyEntity.setState( SchemaEntity.STATE.INVALID );
				}
				packageBodyEntityLst.add( packageBodyEntity );
				
			}
		}
		
		return packageBodyEntityLst;
	}
	
	abstract protected String listSQL( String schemaName );

	// Source of Package Body
	abstract public String getSRC( String schemaName, String packageBodyName ) throws SQLException;
	
}
