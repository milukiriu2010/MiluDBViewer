package milu.entity.schema;

public class SchemaEntityRootFunc extends SchemaEntity
{
	/*
	public SchemaEntityRootFunc()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC );
		
		this.nameId = "ITEM_FUNC";
		this.setName();
		
		this.imageResourceName = "file:resources/images/func_root.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_FUNC;
		
		this.nameId = "ITEM_FUNC";
		this.setName();
		
		this.imageResourceName = "file:resources/images/func_root.png";
	}
}
