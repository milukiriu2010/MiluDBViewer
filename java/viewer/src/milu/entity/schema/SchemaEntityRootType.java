package milu.entity.schema;

public class SchemaEntityRootType extends SchemaEntity
{
	/*
	public SchemaEntityRootType()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_TYPE );
		
		this.nameId = "ITEM_TYPE";
		this.setName();
		
		this.imageResourceName = "file:resources/images/type_root.png";
	}
	*/

	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_TYPE;
		
		this.nameId = "ITEM_TYPE";
		this.setName();
		
		this.imageResourceName = "file:resources/images/type_root.png";
	}

}
