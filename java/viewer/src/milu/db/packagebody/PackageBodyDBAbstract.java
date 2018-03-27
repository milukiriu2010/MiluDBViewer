package milu.db.packagebody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class PackageBodyDBAbstract implements ObjDBInterface 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	@Override
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
	
	@Override
	public List<Map<String,String>> selectDefinition( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	// Source of Package Body
	@Override
	abstract public String getSRC( String schemaName, String packageBodyName ) throws SQLException;
	
}
