package milu.ctrl.sql.generate;

import java.util.Map;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.db.MyDBAbstract;
import milu.db.MyDBOracle;
import milu.db.MyDBPostgres;

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
			// -------------------------------------
			// Oracle
			// -------------------------------------
			if ( myDBAbs instanceof MyDBOracle )
			{
				if ( schemaName.toUpperCase().equals(myDBAbs.getUsername().toUpperCase()) )
				{
					schemaName = null;
				}
			}
			// -------------------------------------
			// PostgreSQL
			// -------------------------------------
			else if ( myDBAbs instanceof MyDBPostgres )
			{
				if ( "public".equals(schemaName) )
				{
					schemaName = null;
				}
			}
			else
			{
				Map<String, String> dbOptsAux = myDBAbs.getDBOptsAux();
				if ( dbOptsAux == null )
				{
					return schemaName;
				}
				
				String dbName = dbOptsAux.get("DBName");
				if ( dbName == null )
				{
					return schemaName;
				}
				
				if ( schemaName.toUpperCase().equals(dbName.toUpperCase()) )
				{
					schemaName = null;
				}
			}
		}
		
		return schemaName;
	}
}
