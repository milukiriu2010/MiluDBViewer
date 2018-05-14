package milu.ctrl.sqlgenerate;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.db.MyDBAbstract;

public abstract class GenerateSQLAbstract 
{
	abstract public String generate( SchemaEntity selectedEntity, MyDBAbstract myDBAbs );
	
	protected String getSchema( SchemaEntity selectedEntity, MyDBAbstract myDBAbs )
	{
		// Search [SCHEMA]
		SearchSchemaEntityInterface searchSchemaVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA);
		selectedEntity.acceptParent(searchSchemaVisitor);
		SchemaEntity schemaEntity = searchSchemaVisitor.getHitSchemaEntity();
		
		String schemaName = null;
		if ( schemaEntity == null )
		{
			schemaName = null;
		}
		else
		{
			schemaName = schemaEntity.getName();
			if ( schemaName.toUpperCase().equals(myDBAbs.getUsername().toUpperCase()) )
			{
				schemaName = null;
			}
		}
		
		return schemaName;
	}
}
