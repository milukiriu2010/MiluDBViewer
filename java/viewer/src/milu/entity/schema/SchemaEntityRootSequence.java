package milu.entity.schema;

public class SchemaEntityRootSequence extends SchemaEntity 
{
	public SchemaEntityRootSequence()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE );
		
		this.nameId = "ITEM_SEQ";
		this.setName();
		
		this.imageResourceName = "file:resources/images/seq_root.png";
	}
}
