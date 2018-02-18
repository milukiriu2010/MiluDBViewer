package milu.entity.schema;

public class SchemaEntityFactory
{
	public static SchemaEntity createInstance( String name, SchemaEntity.SCHEMA_TYPE schemaType )
	{
		if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT )
		{
			return new SchemaEntityRoot( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.SCHEMA )
		{
			return new SchemaEntityEachSchema( name );
		}
		else
		{
			return new SchemaEntity( name, schemaType );
		}
	}
	
	public static SchemaEntity createInstance( SchemaEntity.SCHEMA_TYPE schemaType )
	{
		if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TABLE )
		{
			return new SchemaEntityRootTable();
		}
		else
		{
			return new SchemaEntity( schemaType );
		}
	}
}
