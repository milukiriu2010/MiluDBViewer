package milu.db.obj.schema;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class SchemaDBGeneral extends SchemaDBAbstract 
{
	
	@Override
	public List<SchemaEntity> selectEntityLst( String tmp ) throws SQLException
	{
		List<SchemaEntity>  schemaEntityLst = new ArrayList<>();
		
		DatabaseMetaData md = this.myDBAbs.getMetaData();
		
		try
		(
			ResultSet rs   = md.getSchemas();
		)
		{
			while ( rs.next() )
			{
				SchemaEntity eachSchemaEntity = SchemaEntityFactory.createInstance( rs.getString("TABLE_SCHEM"), SchemaEntity.SCHEMA_TYPE.SCHEMA );
				schemaEntityLst.add( eachSchemaEntity );
			}
			
			return schemaEntityLst;
		}
	}

	@Override
	protected String schemaLstSQL()
	{
		return null;
	}

}
