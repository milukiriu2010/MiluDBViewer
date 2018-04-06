package milu.entity.schema;

public class SchemaEntityRootTrigger extends SchemaEntity 
{
	/*
	public SchemaEntityRootTrigger()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER );
		
		this.nameId = "ITEM_TRIGGER";
		this.setName();
		
		this.imageResourceName = "file:resources/images/trigger_root.png";
	}
	*/

	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER;
		
		this.nameId = "ITEM_TRIGGER";
		this.setName();
		
		this.imageResourceName = "file:resources/images/trigger_root.png";
	}

}
