package milu.entity.schema;

import milu.main.MainController;

public class SchemaEntityFactory
{
	public static SchemaEntity createInstance( String name, SchemaEntity.SCHEMA_TYPE schemaType )
	{
		SchemaEntity schemaEntity = null;
		if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT )
		{
			schemaEntity = new SchemaEntityRoot();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.SCHEMA )
		{
			schemaEntity = new SchemaEntityEachSchema();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.TABLE )
		{
			schemaEntity = new SchemaEntityEachTable();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.INDEX )
		{
			schemaEntity = new SchemaEntityEachIndex();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.INDEX_COLUMN )
		{
			schemaEntity = new SchemaEntityEachIndexColumn();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.VIEW )
		{
			schemaEntity = new SchemaEntityEachView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW )
		{
			schemaEntity = new SchemaEntityEachSystemView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW )
		{
			schemaEntity = new SchemaEntityEachMaterializedView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.FUNC )
		{
			schemaEntity = new SchemaEntityEachFunc();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.AGGREGATE )
		{
			schemaEntity = new SchemaEntityEachAggregate();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.PROC )
		{
			schemaEntity = new SchemaEntityEachProc();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF )
		{
			schemaEntity = new SchemaEntityEachPackageDef();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY )
		{
			schemaEntity = new SchemaEntityEachPackageBody();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.TYPE )
		{
			schemaEntity = new SchemaEntityEachType();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.TRIGGER )
		{
			schemaEntity = new SchemaEntityEachTrigger();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.SEQUENCE )
		{
			schemaEntity = new SchemaEntityEachSequence();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY )
		{
			schemaEntity = new SchemaEntityEachFK();
		}
		else
		{
			return null;
		}
		schemaEntity.setName( name );
		schemaEntity.init();
		return schemaEntity;
	}
	
	public static SchemaEntity createInstance( SchemaEntity.SCHEMA_TYPE schemaType, MainController mainCtrl )
	{
		SchemaEntity schemaEntity = null;
		if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TABLE )
		{
			schemaEntity = new SchemaEntityRootTable();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_INDEX )
		{
			schemaEntity = new SchemaEntityRootIndex();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_VIEW )
		{
			schemaEntity = new SchemaEntityRootView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW )
		{
			schemaEntity = new SchemaEntityRootSystemView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW )
		{
			schemaEntity = new SchemaEntityRootMaterializedView();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_FUNC )
		{
			schemaEntity = new SchemaEntityRootFunc();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE )
		{
			schemaEntity = new SchemaEntityRootAggregate();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PROC )
		{
			schemaEntity = new SchemaEntityRootProc();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF )
		{
			schemaEntity = new SchemaEntityRootPackageDef();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY )
		{
			schemaEntity = new SchemaEntityRootPackageBody();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TYPE )
		{
			schemaEntity = new SchemaEntityRootType();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER )
		{
			schemaEntity = new SchemaEntityRootTrigger();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE )
		{
			schemaEntity = new SchemaEntityRootSequence();
		}
		else if ( schemaType == SchemaEntity.SCHEMA_TYPE.ROOT_ER )
		{
			schemaEntity = new SchemaEntityRootER();
		}
		else
		{
			return null;
		}
		schemaEntity.setMainController(mainCtrl);
		schemaEntity.init();
		return schemaEntity;
	}
}
