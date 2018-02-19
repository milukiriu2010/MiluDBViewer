package milu.entity.schema;

public class SchemaEntityRootProc extends SchemaEntity
{
	public SchemaEntityRootProc()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_PROC );
		
		this.name = "PROCEDURE";
		
		this.imageResourceName = "file:resources/images/proc_root.png";
	}
}
