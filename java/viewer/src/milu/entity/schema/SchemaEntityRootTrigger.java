package milu.entity.schema;

public class SchemaEntityRootTrigger extends SchemaEntity 
{
	public SchemaEntityRootTrigger()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER );
		
		this.name = "TRIGGER";
		
		this.imageResourceName = "file:resources/images/trigger_root.png";
	}
}
