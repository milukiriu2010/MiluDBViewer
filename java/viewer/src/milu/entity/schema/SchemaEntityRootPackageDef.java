package milu.entity.schema;

public class SchemaEntityRootPackageDef extends SchemaEntity
{
	/*
	public SchemaEntityRootPackageDef()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF );
		
		this.nameId = "ITEM_PACKAGE_DEF";
		this.setName();
		
		this.imageResourceName = "file:resources/images/package_def_root.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF;
		
		this.nameId = "ITEM_PACKAGE_DEF";
		this.setName();
		
		this.imageResourceName = "file:resources/images/package_def_root.png";
	}
}
