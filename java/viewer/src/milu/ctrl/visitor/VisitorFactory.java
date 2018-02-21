package milu.ctrl.visitor;

import milu.entity.schema.SchemaEntity;

public class VisitorFactory 
{
	public VisitorInterface createInstance( SchemaEntity.SCHEMA_TYPE searchSchemaType )
	{
		return new SearchSchemaEntityVisitorType( searchSchemaType );
	}
	
	public VisitorInterface createInstance( SchemaEntity.SCHEMA_TYPE searchSchemaType, String  searchName )
	{
		return new SearchSchemaEntityVisitorTypeName( searchSchemaType, searchName );
	}
}
