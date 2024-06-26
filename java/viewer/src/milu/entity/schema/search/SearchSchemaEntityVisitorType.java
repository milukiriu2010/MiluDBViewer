package milu.entity.schema.search;

import milu.entity.schema.SchemaEntity;

public class SearchSchemaEntityVisitorType 
	implements VisitorInterface,
		SearchSchemaEntityInterface 
{
	private SchemaEntity.SCHEMA_TYPE searchSchemaType = null;
	
	private SchemaEntity hitSchemaEntity = null;
	
	public SearchSchemaEntityVisitorType( SchemaEntity.SCHEMA_TYPE searchSchemaType )
	{
		this.searchSchemaType = searchSchemaType;
	}
	
	@Override
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

	@Override
	public void visitParent( SchemaEntity schemaEntity )
	{
		if ( schemaEntity == null )
		{
			return;
		}
		
		SchemaEntity.SCHEMA_TYPE schemaType = schemaEntity.getType();
		if ( schemaType == this.searchSchemaType )
		{
			this.hitSchemaEntity = schemaEntity;
			return;
		}
		
		SchemaEntity parentEntity = schemaEntity.getParentEntity();
		if ( parentEntity != null )
		{
			this.visitParent( parentEntity );
		}
	}
}
