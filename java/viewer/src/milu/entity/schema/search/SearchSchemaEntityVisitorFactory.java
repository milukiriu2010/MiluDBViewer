package milu.entity.schema.search;

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
}
