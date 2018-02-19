package milu.entity.schema;

public class SchemaEntityRootSequence extends SchemaEntity 
{
	public SchemaEntityRootSequence()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE );
		
		this.name = "SEQUENCE";
		
		this.imageResourceName = "file:resources/images/seq_root.png";
	}
}
