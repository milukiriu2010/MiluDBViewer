package milu.entity.schema;

public class SchemaEntityRootAggregate extends SchemaEntity 
{
	public SchemaEntityRootAggregate()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE );
		
		this.nameId = "ITEM_AGGREGATE";
		this.setName();
		
		this.imageResourceName = "file:resources/images/aggregate_root.png";
	}
}
