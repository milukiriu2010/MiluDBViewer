package milu.entity.schema;

public class SchemaEntityRootER extends SchemaEntity
{
	/*
	public SchemaEntityRootER()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_ER );
		
		this.nameId = "ITEM_ER";
		this.setName();
		
		this.imageResourceName = "file:resources/images/ER_root.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_ER;
		
		this.nameId = "ITEM_ER";
		this.setName();
		
		this.imageResourceName = "file:resources/images/ER_root.png";
	}
}
