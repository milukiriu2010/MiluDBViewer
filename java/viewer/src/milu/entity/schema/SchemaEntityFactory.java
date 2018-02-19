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
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.TABLE )
		{
			return new SchemaEntityEachTable( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.VIEW )
		{
			return new SchemaEntityEachView( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW )
		{
			return new SchemaEntityEachMaterializedView( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.FUNC )
		{
			return new SchemaEntityEachFunc( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.PROC )
		{
			return new SchemaEntityEachProc( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF )
		{
			return new SchemaEntityEachPackageDef( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY )
		{
			return new SchemaEntityEachPackageBody( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.TYPE )
		{
			return new SchemaEntityEachType( name );
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.SEQUENCE )
		{
			return new SchemaEntityEachSequence( name );
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
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_INDEX )
		{
			return new SchemaEntityRootIndex();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_VIEW )
		{
			return new SchemaEntityRootView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW )
		{
			return new SchemaEntityRootMaterializedView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_FUNC )
		{
			return new SchemaEntityRootFunc();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PROC )
		{
			return new SchemaEntityRootProc();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF )
		{
			return new SchemaEntityRootPackageDef();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY )
		{
			return new SchemaEntityRootPackageBody();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TYPE )
		{
			return new SchemaEntityRootType();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE )
		{
			return new SchemaEntityRootSequence();
		}
		else
		{
			return new SchemaEntity( schemaType );
		}
	}
}
