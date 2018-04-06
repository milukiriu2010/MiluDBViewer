package milu.entity.schema;

public class SchemaEntityRootView extends SchemaEntity
{
	/*
	public SchemaEntityRootView()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_VIEW );
		
		this.nameId = "ITEM_VIEW";
		this.setName();
		
		this.imageResourceName = "file:resources/images/view_root.png";
	}
	*/

	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_VIEW;
		
		this.nameId = "ITEM_VIEW";
		this.setName();
		
		this.imageResourceName = "file:resources/images/view_root.png";
	}

}
