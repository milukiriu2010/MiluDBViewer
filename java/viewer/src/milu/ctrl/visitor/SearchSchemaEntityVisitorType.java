package milu.ctrl.visitor;

import milu.entity.schema.SchemaEntity;

public class SearchSchemaEntityVisitorType implements VisitorInterface 
{
	private SchemaEntity.SCHEMA_TYPE searchSchemaType = null;
	
	private SchemaEntity hitSchemaEntity = null;
	
	public SearchSchemaEntityVisitorType( SchemaEntity.SCHEMA_TYPE searchSchemaType )
	{
		this.searchSchemaType = searchSchemaType;
	}
	
	public SchemaEntity getHitSchemaEntity()
	{
		return this.hitSchemaEntity;
	}
	
	@Override
	public void visit(SchemaEntity schemaEntity) 
	{
		if ( schemaEntity == null )
		{
			return;
		}
		
		SchemaEntity.SCHEMA_TYPE schemaType = schemaEntity.getType();
		String name = schemaEntity.getName();
		if ( schemaType == this.searchSchemaType )
		{
			this.hitSchemaEntity = schemaEntity;
			return;
		}
		
		for ( SchemaEntity child : schemaEntity.getEntityLst() )
		{
			this.visit( child );
			if ( this.hitSchemaEntity != null )
			{
				return;
			}
		}
	}

}
