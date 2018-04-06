package milu.entity.schema;

public class SchemaEntityRootSystemView extends SchemaEntity 
{
	/*
	public SchemaEntityRootSystemView()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW );
		
		this.nameId = "ITEM_SYSTEM_VIEW";
		this.setName();
		
		this.imageResourceName = "file:resources/images/systemview_root.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW;
		
		this.nameId = "ITEM_SYSTEM_VIEW";
		this.setName();
		
		this.imageResourceName = "file:resources/images/systemview_root.png";
	}

}
