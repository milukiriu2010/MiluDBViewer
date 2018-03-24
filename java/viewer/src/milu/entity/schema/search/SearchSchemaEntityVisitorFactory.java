package milu.entity.schema.search;

import java.util.List;

import milu.entity.schema.SchemaEntity;

public class SearchSchemaEntityVisitorFactory 
{
	public SearchSchemaEntityInterface createInstance( SchemaEntity.SCHEMA_TYPE searchSchemaType )
	{
		return new SearchSchemaEntityVisitorType( searchSchemaType );
	}
	
	public SearchSchemaEntityInterface createInstance( SchemaEntity.SCHEMA_TYPE searchSchemaType, String  searchName )
	{
		return new SearchSchemaEntityVisitorTypeName( searchSchemaType, searchName );
	}
	
	public SearchSchemaEntityInterface createInstance( List<SchemaEntity.SCHEMA_TYPE> searchSchemaTypeLst, String  searchName )
	{
		return new SearchSchemaEntityVisitorTypeListName( searchSchemaTypeLst, searchName );
	}
}
