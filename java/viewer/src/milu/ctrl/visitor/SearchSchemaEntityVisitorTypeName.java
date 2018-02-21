package milu.ctrl.visitor;

import milu.entity.schema.SchemaEntity;

public class SearchSchemaEntityVisitorTypeName 
	implements 
		VisitorInterface, 
		SearchSchemaEntityInterface 
{
	private SchemaEntity.SCHEMA_TYPE searchSchemaType = null;
	
	private String  searchName = null;
	
	private SchemaEntity hitSchemaEntity = null;
	
	public SearchSchemaEntityVisitorTypeName( SchemaEntity.SCHEMA_TYPE searchSchemaType, String  searchName )
	{
		this.searchSchemaType = searchSchemaType;
		this.searchName       = searchName;
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
		String name = schemaEntity.getName();
		if ( ( schemaType == this.searchSchemaType ) && name.equals(this.searchName) )
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
