package milu.entity.schema;

public class SchemaEntityRootProc extends SchemaEntity
{
	/*
	public SchemaEntityRootProc()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_PROC );
		
		this.nameId = "ITEM_PROC";
		this.setName();
		
		this.imageResourceName = "file:resources/images/proc_root.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.ROOT_PROC;
		
		this.nameId = "ITEM_PROC";
		this.setName();
		
		this.imageResourceName = "file:resources/images/proc_root.png";
	}
}
