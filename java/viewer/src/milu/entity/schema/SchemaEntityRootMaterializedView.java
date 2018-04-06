package milu.entity.schema;

public class SchemaEntityRootMaterializedView extends SchemaEntity
{
	/*
	public SchemaEntityRootMaterializedView()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		
		this.nameId = "ITEM_MATERIALIZED_VIEW";
		this.setName();
		
		this.imageResourceName = "file:resources/images/materialized_view_root.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW;
		
		this.nameId = "ITEM_MATERIALIZED_VIEW";
		this.setName();
		
		this.imageResourceName = "file:resources/images/materialized_view_root.png";
	}
}
